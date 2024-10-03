package com.example.projetopdm

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetopdm.model.dados.UsuarioDAO
import com.example.projetopdm.ui.screens.TelaCadastro
import com.example.projetopdm.ui.screens.TelaDeBusca
import com.example.projetopdm.ui.screens.TelaLogin
import com.example.projetopdm.ui.screens.TelaPerfil
import com.example.projetopdm.ui.screens.TelaPrincipal
import com.example.projetopdm.ui.screens.TelaFavoritos
import com.example.projetopdm.ui.theme.ProjetoPDMTheme
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProjetoPDMTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                val userId = currentBackStackEntry?.arguments?.getString("userId") ?: ""

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.logo),
                                        contentDescription = "MyCine Logo",
                                        modifier = Modifier
                                            .size(200.dp)
                                            .padding(8.dp)
                                    )
                                }
                            },
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    },
                    bottomBar = {
                        if (currentRoute != "login" && currentRoute != "signup") {
                            BottomNavigationBar(navController, userId)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            TelaLogin(
                                modifier = Modifier.padding(innerPadding),
                                onSignInClick = { id ->
                                    navController.navigate("principal/$id")
                                },
                                onSignUpClick = {
                                    navController.navigate("signup")
                                },
                                usuarioDAO = UsuarioDAO()
                            )
                        }
                        composable("signup") {
                            TelaCadastro(
                                onSignUpClick = {
                                    navController.navigate("login")
                                },
                                onSignInClick = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable("principal/{userId}") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            TelaPrincipal(
                                modifier = Modifier.padding(innerPadding),
                                userId = userId,
                                onLogoffClick = {
                                    navController.navigate("perfil/$userId")
                                },
                                navController = navController
                            )
                        }
                        composable("principal/{userId}/buscar") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            TelaDeBusca(
                                modifier = Modifier.padding(innerPadding),
                            )
                        }
                        composable("principal/{userId}/TelaFavoritos") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            TelaFavoritos(
                                userId = userId,
                                navController = navController,
                                modifier = Modifier.padding(innerPadding),
                            )
                        }
                        composable("perfil/{userId}") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            val activity = LocalContext.current as Activity // Obtendo o contexto atual como Activity
                            TelaPerfil(
                                userId = userId,
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    navController.navigate("login") {
                                        popUpTo("principal") { inclusive = true }
                                    }
                                },
                                activity = activity // Passando a Activity corretamente
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, userId: String) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navegação de ícones e textos para cada seção
            IconWithText(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = { navController.navigate("principal/$userId") }
            )
            IconWithText(
                icon = Icons.Default.Search,
                label = "Buscar",
                onClick = { navController.navigate("principal/$userId/buscar") }
            )
            IconWithText(
                icon = Icons.Default.Menu,
                label = "Minhas Listas",
                onClick = { navController.navigate("principal/$userId/TelaFavoritos") }
            )
            IconWithText(
                icon = Icons.Default.AccountCircle,
                label = "Perfil",
                onClick = { navController.navigate("perfil/$userId") }
            )
        }
    }
}

@Composable
fun IconWithText(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            tint = Color(0xFF00186F),
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjetoPDMTheme {
        TelaLogin(
            onSignInClick = {},
            onSignUpClick = {},
            usuarioDAO = UsuarioDAO() // Adicionando o parâmetro `usuarioDAO`
        )
    }
}
