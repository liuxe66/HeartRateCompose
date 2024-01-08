package com.atom.heartratecompose.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.atom.heartratecompose.ui.theme.ActBg
import com.atom.heartratecompose.ui.theme.Color111
import com.atom.heartratecompose.ui.theme.Color999
import com.atom.heartratecompose.ui.theme.hightResult
import com.atom.heartratecompose.ui.theme.lowResult
import com.atom.heartratecompose.ui.theme.normalResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 *  author : liuxe
 *  date : 2024/1/4 11:31
 *  description :
 */

@Composable
fun ResultScreen(controller: NavController,heartRate: Int) {

    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true
    }

    ResultPreview(heartRate)

}

@Preview
@Composable
fun ResultPreview(heartRate: Int = 50) {
    Column(modifier = Modifier.fillMaxSize().background(ActBg)) {
        Spacer(modifier = Modifier.height(40.dp))
        PlayHeart(
            modifier = Modifier
                .fillMaxWidth(),
            heartRate = heartRate
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "您的测量结果", fontSize = 14.sp, color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = when (heartRate) {
                    in 0..60 -> "慢"
                    in 61..100 -> "正常"
                    else -> "快"
                },
                fontSize = 24.sp,
                color = when (heartRate) {
                    in 0..60 -> lowResult
                    in 61..100 -> normalResult
                    else -> hightResult
                },
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "参考范围", fontSize = 14.sp, color = Color.Black
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = lowResult,
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Text(
                        text = "慢",
                        fontSize = 14.sp,
                        color = if (heartRate in 0 .. 59){
                            Color111
                        } else {
                            Color999
                        }
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = "<60 BPM",
                    fontSize = 14.sp,
                    color = if (heartRate in 0 .. 59){
                        Color111
                    } else {
                        Color999
                    }
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = normalResult,
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Text(
                        text = "正常",
                        fontSize = 14.sp,
                        color = if (heartRate in 60 .. 100){
                            Color111
                        } else {
                            Color999
                        }
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = "60-100 BPM",
                    fontSize = 14.sp,
                    color = if (heartRate in 60 .. 100){
                        Color111
                    } else {
                        Color999
                    }
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = hightResult,
                                shape = RoundedCornerShape(16.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    Text(
                        text = "快",
                        fontSize = 14.sp,
                        color = if (heartRate >100){
                            Color111
                        } else {
                            Color999
                        }
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = ">100 BPM",
                    fontSize = 14.sp,
                    color = if (heartRate >100){
                        Color111
                    } else {
                        Color999
                    }
                )
            }

        }
    }

}