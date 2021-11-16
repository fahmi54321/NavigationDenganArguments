package com.android.navigationdenganarguments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.navigationdenganarguments.ui.theme.NavigationDenganArgumentsTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationDenganArgumentsTheme {

                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable(
                            "home"
                        ) {
                            SelectionScreen(navController)
                        }

                        //route show image and scale
                        composable(
                            "show/{imageResource}/{scale}?caption={caption}",
                            arguments = listOf(
                                navArgument("imageResource") {
                                    type = NavType.IntType
                                },
                                navArgument("scale") {
                                    type = NavType.FloatType
                                },
                                navArgument("caption") {
                                    type = NavType.StringType;defaultValue = "No caption"
                                }
                            )
                        ) {
                            val image = it.arguments?.getInt("imageResource")
                            val scale = it.arguments?.getFloat("scale")
                            val caption = it.arguments?.getString("caption")

                            if (image != null && scale != null) {
                                ShowScreen(image, scale, caption ?: "")
                            }
                        }

                        //route show text
                        composable(
                            "greet?name={name}",
                            arguments = listOf(
                                navArgument("name") {
                                    type = NavType.StringType;defaultValue = "Your name"
                                }
                            )
                        ) {
                            val name = it.arguments?.getString("name")
                            GreetingScreen(name ?: "")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionScreen(navController: NavHostController) {

    val images = listOf(R.drawable.pineapple, R.drawable.apple, R.drawable.banana)
    var selectImage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.LightGray,
                    RoundedCornerShape(5.dp)
                )
                .clip(RoundedCornerShape(5.dp))
        ) {
            Text(
                text = "Navigate with Arguments",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                for (i in images.indices) {
                    val image = images[i]
                    Image(
                        painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp)
                            .selectable(
                                selected = selectImage == i,
                                onClick = {
                                    selectImage = i
                                }
                            )
                            .background(
                                if (i == selectImage) {
                                    Color.Magenta
                                } else {
                                    Color.Unspecified
                                }
                            )
                    )
                }
            }

            val (slide, onSlideChange) = remember { mutableStateOf(0f) }
            Slider(
                value = slide,
                onValueChange = {
                    onSlideChange(it)
                },
                valueRange = 0f..3f,
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp
                    )
            )

            Text(
                text = "Scale :$slide",
                modifier = Modifier
                    .padding(8.dp)
            )

            val (caption, onCaptionChange) = remember { mutableStateOf("") }

            OutlinedTextField(
                value = caption,
                onValueChange = onCaptionChange,
                label = {
                    Text(
                        text = "Image caption"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Button(
                onClick = {
                    navController.navigate("show/${images[selectImage]}/$slide?caption=$caption")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Text(text = "Show image")
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            val (name, onNameChange) = remember { mutableStateOf("") }

            Text(
                text = "Navigate with Optional Arguments",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = "Name") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        null
                    )
                }
            )

            Button(
                onClick = {
                    navController.navigate("greet?name=$name")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Greet Me",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }
    }
}

@Composable
fun ShowScreen(image: Int, scale: Float, caption: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .shadow(2.dp)
    ) {
        Image(
            painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .scale(scale)
                .align(Alignment.Center)
        )

        Text(
            text = caption,
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
        )
    }
}

@Composable
fun GreetingScreen(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Welcome  $name",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
    }
}
