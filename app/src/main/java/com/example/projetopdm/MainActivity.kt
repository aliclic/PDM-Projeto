package com.example.projetopdm

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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetopdm.ui.telas.TelaCadastro
import com.example.projetopdm.ui.telas.TelaLogin
import com.example.projetopdm.ui.telas.TelaPerfil
import com.example.projetopdm.ui.telas.TelaPrincipal
import com.example.projetopdm.ui.theme.ProjetoPDMTheme

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
                                }
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
                                }
                            )
                        }
                        composable("perfil/{userId}") { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            TelaPerfil(
                                userId = userId,
                                modifier = Modifier.padding(innerPadding),
                                onBackClick = {
                                    navController.navigate("login") {
                                        popUpTo("principal") { inclusive = true }
                                    }
                                }
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    tint = Color(0xFF00186F),
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(32.dp).clickable {
                        navController.navigate("principal/$userId")
                    }
                )
                Text("Home")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    tint = Color(0xFF00186F),
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favoritos",
                    modifier = Modifier.size(32.dp).clickable {
                        // Navegação para uma tela de Favoritos pode ser adicionada aqui
                    }
                )
                Text("Favoritos")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    tint = Color(0xFF00186F),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(32.dp).clickable {
                        navController.navigate("perfil/$userId")
                    }
                )
                Text("Perfil")
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    TelaLogin(modifier, onSignInClick = {}, onSignUpClick = {})
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjetoPDMTheme {
        TelaLogin(onSignInClick = {}, onSignUpClick = {})
    }
}

