package com.example.projetopdm.model.dados

import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.navigation.NavController
import com.example.projetopdm.model.Movie
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

class UsuarioDAO{
    val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val collectionRef = db.collection("usuarios")

    fun signIn(email: String, senha: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Recupera o ID do usuário autenticado
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    onSuccess(userId)
                } else {
                    // Trata o erro de autenticação
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Sua senha ou email estão errados!"
                        is FirebaseAuthInvalidUserException -> "Usuário não encontrado"
                        else -> "Erro ao realizar login, tente novamente"
                    }
                    onError(errorMessage)
                }
            }
    }

    fun buscar(callback: (List<Usuario>) -> Unit) {
        Log.d("Firestore", "Iniciando busca de usuários")
        db.collection("usuarios").get()
            .addOnSuccessListener { document ->
                Log.d("Firestore", "Documentos retornados com sucesso")
                val usuarios = document.toObjects<Usuario>()
                Log.d("Firestore", "Usuários convertidos: ${usuarios.size} encontrados")
                callback(usuarios)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Erro ao buscar usuários", exception)
                callback(emptyList())
            }
    }

    fun buscarPorId(userId: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val usuario = documents.documents[0].toObject(Usuario::class.java)
                    callback(usuario)
                } else {
                    callback(null) // Não encontrou o usuário
                }
            }
            .addOnFailureListener {
                callback(null) // Houve uma falha na busca
            }
    }

    fun adicionar(usuario: Usuario, callback: (Usuario) -> Unit) {
        val userId = usuario.id // O ID do usuário já deve vir do Firebase Authentication
        db.collection("usuarios").document(userId).set(usuario)
            .addOnSuccessListener {
                callback(usuario) // Retorna o usuário com o ID do Firebase Authentication
            }
            .addOnFailureListener {
                callback(usuario)
            }
    }

    fun atualizarUsuario(
        userId: String,
        novosDados: Map<String, Any>,
        novaSenha: String?, // Agora, apenas a senha pode ser atualizada
        senhaAtual: String,  // Senha atual necessária para reautenticação
        callback: (Boolean) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("usuarios").document(userId)

        if (currentUser != null && currentUser.email != null) {
            reautenticarUsuario(currentUser.email!!, senhaAtual) { reautenticado ->
                if (reautenticado) {
                    // Atualizar dados no Firestore
                    docRef.update(novosDados)
                        .addOnSuccessListener {
                            val updateSenhaTask = if (novaSenha != null) {
                                currentUser.updatePassword(novaSenha)
                            } else null

                            if (updateSenhaTask != null) {
                                updateSenhaTask.addOnSuccessListener {
                                    callback(true) // Sucesso ao atualizar senha e Firestore
                                }.addOnFailureListener { e ->
                                    Log.e("UPDATE_AUTH_ERROR", "Erro ao atualizar senha", e)
                                    callback(false) // Falha ao atualizar senha no Firebase Auth
                                }
                            } else {
                                callback(true) // Somente Firestore foi atualizado
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("UPDATE_FIRESTORE_ERROR", "Erro ao atualizar Firestore", e)
                            callback(false) // Falha ao atualizar Firestore
                        }
                } else {
                    callback(false) // Falha na reautenticação
                }
            }
        } else {
            callback(false) // Nenhum usuário autenticado
        }
    }



    fun reautenticarUsuario(email: String, senha: String, onComplete: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val credential = EmailAuthProvider.getCredential(email, senha)

            currentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    onComplete(true) // Reautenticação bem-sucedida
                }
                .addOnFailureListener { e ->
                    Log.e("REAUTH_ERROR", "Erro ao reautenticar", e)
                    onComplete(false) // Falha na reautenticação
                }
        } else {
            onComplete(false) // Nenhum usuário autenticado
        }
    }




    fun deletarUsuario(userId: String, callback: (Boolean) -> Unit) {
        val userDocumentRef = collectionRef.document(userId)
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Deleta o documento do Firestore
        userDocumentRef.delete()
            .addOnSuccessListener {
                // Se o documento for deletado com sucesso, deletamos o usuário no Firebase Authentication
                currentUser?.delete()
                    ?.addOnSuccessListener {
                        callback(true)
                    }
                    ?.addOnFailureListener { exception ->
                        println("Erro ao deletar o usuário do Firebase Authentication: ${exception.message}")
                        callback(false)
                    }
            }
            .addOnFailureListener { exception ->
                // Falha ao deletar o documento do Firestore
                println("Erro ao deletar o documento do Firestore: ${exception.message}")
                callback(false)
            }
    }

    fun getUserMovieLists(userId: String, onComplete: (List<ListaFilmes>) -> Unit) {
        collectionRef.document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(Usuario::class.java)
                user?.let {
                    onComplete(it.filmes) // Retorna a lista de filmes do usuário
                } ?: onComplete(emptyList())
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao carregar listas de filmes", exception)
                onComplete(emptyList())
            }
    }

    fun getUserFavorites(userId: String, onFavoritesLoaded: (List<Int>) -> Unit) {
        collectionRef.document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(Usuario::class.java)
                user?.let {
                    val favoriteMovieIds = it.filmes.flatMap { lista -> lista.filmes } // Extrai apenas os IDs dos filmes
                    onFavoritesLoaded(favoriteMovieIds)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user favorites", exception)
                onFavoritesLoaded(emptyList())
            }
    }


    fun addNewList(userId: String, listName: String, onComplete: (Boolean) -> Unit) {
        // Gerar um novo ID para a lista de filmes
        val newListId = collectionRef.document().id

        // Criar uma nova instância de ListaFilmes
        val newList = ListaFilmes(id = newListId, titulo = listName)

        // Referência ao documento do usuário
        val userRef = collectionRef.document(userId)

        // Adicionar a nova lista de filmes ao array "filmes" do usuário
        userRef.update(
            "filmes", FieldValue.arrayUnion(newList)
        ).addOnSuccessListener {
            // Log para depuração
            Log.d("Firestore", "Lista de Filmes: ${newList.titulo}, ID: ${newList.id}")
            Log.d("Firestore", "Nova lista adicionada com sucesso")
            onComplete(true)
        }.addOnFailureListener { exception ->
            // Log de erro
            Log.e("Firestore", "Erro ao adicionar nova lista", exception)
            onComplete(false)
        }
    }



    fun buscarListaFilmesPorId(userId: String, listaId: String, onComplete: (ListaFilmes?) -> Unit) {
        val userRef = db.collection("usuarios").document(userId)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val usuario = documentSnapshot.toObject(Usuario::class.java)

                    // Encontra a lista de filmes pelo ID
                    val listaFilmes = usuario?.filmes?.find { it.id == listaId }

                    if (listaFilmes != null) {
                        onComplete(listaFilmes) // Retorna a lista de filmes encontrada
                    } else {
                        Log.e("Firestore", "Lista de filmes com o id $listaId não encontrada")
                        onComplete(null) // Lista não encontrada
                    }
                } else {
                    Log.e("Firestore", "Usuário com o id $userId não encontrado")
                    onComplete(null) // Usuário não encontrado
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao buscar documento do usuário", exception)
                onComplete(null) // Erro na busca
            }
    }


    fun adicionarFilmeNaLista(userId: String, listaId: String, filmeId: Int, onComplete: (Boolean) -> Unit) {
        // Primeiro, busca a lista de filmes pelo ID
        buscarListaFilmesPorId(userId, listaId) { listaFilmes ->
            if (listaFilmes != null) {
                Log.d("Firestore", "Lista encontrada! ID: ${listaFilmes.id}, Título: ${listaFilmes.titulo}")
                val userRef = db.collection("usuarios").document(userId)

                userRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val usuario = documentSnapshot.toObject(Usuario::class.java)

                            // Adiciona o ID do filme à lista de forma correta, sem duplicar
                            val filmesAtualizados = listaFilmes.filmes.toMutableList().apply {
                                if (!contains(filmeId)) {
                                    add(filmeId) // Adiciona o filme se ele não estiver presente
                                }
                            }

                            // Cria uma cópia da lista de filmes com o ID correto e os filmes atualizados
                            val listaAtualizada = listaFilmes.copy(filmes = filmesAtualizados)

                            // Cria uma lista modificada, substituindo apenas a lista com o id correspondente
                            val listasAtualizadas = usuario?.filmes?.map {
                                if (it.id == listaId) listaAtualizada else it
                            }

                            // Faz o update no Firestore, atualizando somente a lista correta
                            userRef.update("filmes", listasAtualizadas)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Filme adicionado à lista com sucesso")
                                    onComplete(true)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Firestore", "Erro ao atualizar a lista de filmes", exception)
                                    onComplete(false)
                                }
                        } else {
                            Log.e("Firestore", "Usuário com o id $userId não encontrado")
                            onComplete(false) // Usuário não encontrado
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Erro ao buscar documento do usuário", exception)
                        onComplete(false)
                    }
            } else {
                Log.e("Firestore", "Lista de filmes com o id $listaId não encontrada")
                onComplete(false) // Lista não encontrada
            }
        }
    }


    fun logout(onComplete: (Boolean) -> Unit) {
        try {
            auth.signOut()
            onComplete(true)
        } catch (e: Exception) {
            println("Erro ao fazer logout: ${e.message}")
            onComplete(false)
        }
    }


}