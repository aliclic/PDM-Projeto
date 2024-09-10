package com.example.projetopdm.model.dados

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

class UsuarioDAO{
    val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val collectionRef = db.collection("usuarios")

    fun buscar(callback: (List<Usuario>) -> Unit) {
        db.collection("usuarios").get()
            .addOnSuccessListener { document ->
                val usuarios = document.toObjects<Usuario>()
                callback(usuarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun buscarPorNome(nome: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios").whereEqualTo("nome", nome).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    val usuario = document.documents[0].toObject<Usuario>()
                    callback(usuario)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun buscarPorNickName(nickName: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios").whereEqualTo("nickName", nickName).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    val usuario = document.documents[0].toObject<Usuario>()
                    callback(usuario)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun buscarPorId(id: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios").document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val usuario = document.toObject<Usuario>()
                    callback(usuario)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun adicionar(usuario: Usuario, callback: (Usuario) -> Unit) {
        db.collection("usuarios").add(usuario)
            .addOnSuccessListener { documentReference ->
                val updatedUsuario = usuario.copy(id = documentReference.id)
                callback(updatedUsuario)
            }
            .addOnFailureListener {
                callback(usuario)
            }
    }

    fun atualizarUsuario(userId: String, novosDados: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        val userDocumentRef = collectionRef.document(userId)

        // Atualiza múltiplos campos no documento do usuário
        userDocumentRef.update(novosDados)
            .addOnSuccessListener {
                // Chama o callback com 'true' em caso de sucesso
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                // Log de erro em caso de falha
                println("Erro ao atualizar o usuário: ${exception.message}")
                // Chama o callback com 'false' em caso de falha
                onComplete(false)
            }
    }



    fun deletarUsuario(userId: String, callback: (Boolean) -> Unit) {
        val userDocumentRef = collectionRef.document(userId)

        // Deleta o documento do usuário
        userDocumentRef.delete()
            .addOnSuccessListener {
                // Chama o callback com 'true' em caso de sucesso
                callback(true)
            }
            .addOnFailureListener { exception ->
                // Log de erro em caso de falha
                println("Erro ao deletar o usuário: ${exception.message}")
                // Chama o callback com 'false' em caso de falha
                callback(false)
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