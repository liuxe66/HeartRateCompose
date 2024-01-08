package com.atom.heartratecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atom.heartratecompose.ui.screen.HomeScreen
import com.atom.heartratecompose.ui.screen.PlayScreen
import com.atom.heartratecompose.ui.screen.ResultScreen
import com.atom.heartratecompose.ui.theme.HeartRateComposeTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HeartRateComposeTheme {
                ProvideWindowInsets {
                    val uiController = rememberSystemUiController()
                    SideEffect {
                        uiController.setStatusBarColor(Color.Transparent)
                        uiController.statusBarDarkContentEnabled = true
                    }

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController, startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(controller = navController)
                        }
                        composable("play"){
                            PlayScreen(controller = navController)
                        }
                        composable(
                            "result?heartRate={heartRate}",
                            arguments = listOf(navArgument("heartRate") {
                                type = NavType.IntType
                                defaultValue = 0
                            })
                        ) {
                            ResultScreen(controller = navController,
                                it.arguments?.getInt("heartRate")!!
                            )
                        }
                    }
                }
            }
        }
    }
}


enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.85f else 1f, label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

fun Modifier.withClick(onClick: () -> Unit) = composed {

    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
