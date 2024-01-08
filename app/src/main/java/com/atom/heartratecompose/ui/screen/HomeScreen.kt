package com.atom.heartratecompose.ui.screen

import android.widget.Space
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.atom.heartratecompose.R
import com.atom.heartratecompose.ui.theme.ActBg
import com.atom.heartratecompose.ui.theme.Color111
import com.atom.heartratecompose.ui.theme.Color999
import com.atom.heartratecompose.ui.theme.hightResult
import com.atom.heartratecompose.ui.theme.lowResult
import com.atom.heartratecompose.ui.theme.normalResult
import com.atom.heartratecompose.ui.vm.HeartRateVM
import com.atom.heartratecompose.utils.format
import com.atom.heartratecompose.utils.logE
import com.atom.heartratecompose.withClick
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDateTime

/**
 *  author : liuxe
 *  date : 2024/1/4 11:31
 *  description :
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(controller: NavController) {
    var heartRateVM: HeartRateVM = viewModel()
    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true

    }


    val bottomState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomState,
        sheetContent = {
            val listData = heartRateVM.pager.collectAsLazyPagingItems()

            listData.refresh()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
                items(listData) {
                    HomeBottom(modifier = Modifier
                        .padding(
                            start = 24.dp, end = 24.dp, bottom = 12.dp
                        )
                        .fillMaxWidth()
                        .height(100.dp),
                        rateValue = it?.heartRate ?: 0,
                        dateValue = it?.date?: LocalDateTime.now(),
                        onClick = {})
                }
            }

        },
        sheetPeekHeight = if (heartRateVM.pager.collectAsLazyPagingItems().itemCount == 0) 0.dp else 160.dp,
        sheetContainerColor = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        HeartRate(controller)
    }


}

@Preview
@Composable
fun HeartRate(controller: NavController = NavController(LocalContext.current)) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val scaleValue by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        ),
        label = "",
    )

    var rateValue by remember {
        mutableStateOf(1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ActBg),
    ) {
        //顶部
        HomeTop(modifier = Modifier.fillMaxWidth())

        //居中按钮
        HomeHeartBtn(modifier = Modifier
            .fillMaxWidth()
            .scale(scaleValue)
            .align(Alignment.Center)
            .offset(y = -20.dp), onClick = {
            controller.navigate("play")
        })

        //底部历史


    }

}

@Composable
fun HomeTop(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_12),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.ic_usecase),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth()
        )

    }
}


@Composable
fun HomeHeartBtn(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {

    Box(
        modifier = modifier.withClick { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_measure_start),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            modifier = Modifier.height(70.dp),
            text = "开始",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 86.dp),
            text = "心率检测",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(heightDp = 100)
@Composable
fun HomeBottom(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    rateValue: Int = 90,
    dateValue:LocalDateTime = LocalDateTime.now()
) {


    BoxWithConstraints {


    }

    Card(
        modifier = if (rateValue == 0) modifier else modifier.withClick {
            onClick()
        },
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ActBg),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_home_heart),
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd)
            )
            if (rateValue != 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .width(60.dp),
                        text = rateValue.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.End
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_smallheart),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                        )
                        Text(
                            modifier = Modifier, text = "bpm", fontSize = 12.sp, color = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(
                                start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp
                            )
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = when (rateValue) {
                                            in 0..59 -> lowResult
                                            in 60..100 -> normalResult
                                            else -> hightResult
                                        }, shape = RoundedCornerShape(3.dp)
                                    )
                            )
                            Spacer(
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = when (rateValue) {
                                    in 0..59 -> "慢"
                                    in 60..100 -> "正常"
                                    else -> "快"
                                }, fontSize = 14.sp, color = Color111
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = dateValue.format(),
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }

                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart),
                    text = "暂无记录",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }


        }
    }
}