package com.atom.heartratecompose.ui.screen

import android.Manifest
import android.content.Context
import android.graphics.ImageFormat
import android.media.Image
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.atom.heartratecompose.R
import com.atom.heartratecompose.app.HeartRateApp
import com.atom.heartratecompose.ui.theme.ActBg
import com.atom.heartratecompose.ui.vm.HeartRateVM
import com.atom.heartratecompose.utils.ImageProcessing
import com.atom.heartratecompose.utils.logE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList

/**
 *  author : liuxe
 *  date : 2024/1/4 11:31
 *  description :
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlayScreen(controller: NavController) {


    val uiController = rememberSystemUiController()
    SideEffect {
        uiController.setStatusBarColor(Color.Transparent)
        uiController.statusBarDarkContentEnabled = true
    }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }



    if (cameraPermissionState.status.isGranted) {
        //接受拍照的授权
        PlayPreview(controller)
    } else {
        //未授权，显示未授权的界面
        NoPermissionScreen(cameraPermissionState)
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoPermissionScreen(cameraPermissionState: PermissionState) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val message = if (cameraPermissionState.status.shouldShowRationale) {
                "未获取照相机权限导致无法使用照相机功能"
            } else {
                "请授权照相机的权限"
            }
            Text(message)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                cameraPermissionState.launchPermissionRequest()
            }) {
                Text("请求授权")
            }
        }
    }

}

@Composable
fun PlayPreview(controller: NavController) {
    var heartRate by remember {
        mutableStateOf(0)
    }

    var isStart by remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val scaleValue by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        ),
        label = "",
    )

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("heartrate.json"))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ActBg)
    ) {
        Box {
            HomeTop(modifier = Modifier.fillMaxWidth())
            CameraPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = 60.dp),
                startChange = { res ->
                    isStart = res
                },
                heareRateChange = { res ->
                    heartRate = res
                },
                controller
            )
        }
//        PlayHeart(
//            modifier = Modifier
//                .fillMaxWidth()
//                .scale(scaleValue)
//                .offset(y=0.dp),
//            heartRate = heartRate
//        )

        if (isStart) {
            LottieAnimation(
                composition = composition,
                isPlaying = true,
                iterations = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -80.dp)
            )
        }

    }

}

@Composable
fun PlayHeart(
    modifier: Modifier = Modifier, heartRate: Int
) {

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_measure_start),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            modifier = Modifier.height(70.dp),
            text = if (heartRate == 0) "--" else heartRate.toString(),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 86.dp),
            text = "bpm",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


@Preview
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    startChange: (isStart: Boolean) -> Unit = {},
    heareRateChange: (heartRate: Int) -> Unit = {},
    controller: NavController = NavController(LocalContext.current)
) {
    var heartRateVM: HeartRateVM = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    var camera: Camera? = null
    var averageData = LinkedList<Int>()
    var endTime = 0L
    var allTime = 20
    var msg by remember {
        mutableStateOf("正在测量")
    }
    var progress by remember {
        mutableStateOf(0)
    }
    var cutdown by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center
        ) {
            androidx.compose.material.Card(
                modifier = Modifier.size(100.dp), shape = RoundedCornerShape(70.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AndroidView(factory = { context ->
                        val previewView = PreviewView(context).apply {
                            //设置布局宽度和高度占据全屏
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            //设置渲染的实现模式
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            //设置缩放方式
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }
                        val executor = ContextCompat.getMainExecutor(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            val preview = androidx.camera.core.Preview.Builder().build()

                            //图像分析
                            val imageAnalysis = ImageAnalysis.Builder().build().also {
                                it.setAnalyzer(executor) { image ->
                                    processImage(image = image,
                                        msgChange = { res ->
                                            //提示信息
                                            msg = res
                                        },
                                        averageData = averageData,
                                        averageDataChange = { res -> averageData.add(res) },
                                        endTime = endTime,
                                        endTimeChange = { res -> endTime = res },
                                        allTime = allTime,
                                        progressChange = { res ->
                                            //进度
                                            progress = res

                                        },
                                        startChange = { res ->
                                            startChange.invoke(res)
                                        },
                                        restart = {
                                            averageData.clear()
                                            endTime = 0L
                                            progress = 0
                                        },
                                        cutdownChange = {
                                            cutdown = it
                                        },
                                        finished = {
                                            "====finished===".logE()

                                            endTime = 0L
                                            progress = 0

                                            scope.launch {
                                                delay(200)
                                                //关闭闪光灯
                                                camera?.cameraControl?.enableTorch(false)
                                                //处理数据
                                                var heartRate = processData(averageData) * (60/allTime)
                                                heareRateChange.invoke(heartRate)
                                                heartRateVM.insertHeartRate(heartRate)
                                                "====insertHeartRate===".logE()
                                                //跳转到结果界面
                                                controller.navigate("result?heartRate=${heartRate}") {
                                                    controller.popBackStack()
                                                }

                                            }

                                        })
                                    image.close()
                                }
                            }

                            cameraProvider.unbindAll()
                            camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview,// imageCapture,
                                imageAnalysis
                            )
                            camera?.cameraControl?.enableTorch(true)
                            preview.setSurfaceProvider(previewView.surfaceProvider)
                        }, executor)
                        previewView
                    }, modifier = Modifier.fillMaxSize())
                }
            }

//            CircularProgressIndicator(
//                progress = progress.toFloat() / 100,
//                modifier = Modifier.size(80.dp),
//                color = Color.LightGray,
//                strokeWidth = 10.dp,
//                strokeCap = StrokeCap.Round
//            )

            if (cutdown in 1..allTime){
                Text(
                    text = "${allTime - cutdown}S",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                vibrate()
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = msg,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (progress == 0) "请将手指按到摄像头上" else "检测进度：${progress}%",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray
        )
    }
}


@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun processImage(
    image: ImageProxy, averageData: LinkedList<Int>,//获取averageData
    msgChange: (msg: String) -> Unit, averageDataChange: (imgAvg: Int) -> Unit, //修改averageData
    endTime: Long,//开始时间
    endTimeChange: (starTime: Long) -> Unit,//修改开始时间
    allTime: Int,//总时间
    progressChange: (progress: Int) -> Unit,//进度 目前使用秒数
    cutdownChange: (second: Int) -> Unit,//进度 目前使用秒数
    startChange: (isStart: Boolean) -> Unit,//开始测量
    restart: () -> Unit,//重新开始
    finished: () -> Unit//完成测量
) {
    val width: Int = image.width
    val height: Int = image.height
    var nv21 = YUV420toNV21(image.image!!)

    val imgAvg: Int = ImageProcessing.decodeYUV420SPtoRedAvg(
        nv21, height, width
    )
    if (imgAvg == 0 || imgAvg == 255 || imgAvg < 190) {
        // 心率异常
        //需要重现开始
        msgChange.invoke("未检测到手指")
        startChange.invoke(false)
        restart.invoke()
        return
    } else {
        startChange.invoke(true)
        msgChange.invoke("测量中...")
    }
    if (averageData.peekLast() == null || averageData.peekLast() != imgAvg) {
        averageDataChange.invoke(imgAvg)
    }

    if (endTime == 0L) {
        endTimeChange.invoke(System.currentTimeMillis() + 1000 * allTime)
        var progress = 0
        cutdownChange.invoke(0)
        progressChange.invoke(progress)
    } else if (System.currentTimeMillis() < endTime) {
        var curTime = (System.currentTimeMillis() - (endTime - 1000 * allTime)) / 1000
        var progress = (curTime.toFloat() / allTime * 100).toInt()
        cutdownChange.invoke(curTime.toInt())
        progressChange.invoke(progress)
    } else {
        var progress = 100
        cutdownChange.invoke(allTime)
        progressChange.invoke(progress)
        finished.invoke()
        return
    }


}

fun YUV420toNV21(image: Image): ByteArray? {
    val crop = image.cropRect
    val format = image.format
    val width = crop.width()
    val height = crop.height()
    val planes = image.planes
    val data = ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
    val rowData = ByteArray(planes[0].rowStride)
    var channelOffset = 0
    var outputStride = 1
    for (i in planes.indices) {
        when (i) {
            0 -> {
                channelOffset = 0
                outputStride = 1
            }

            1 -> {
                channelOffset = width * height + 1
                outputStride = 2
            }

            2 -> {
                channelOffset = width * height
                outputStride = 2
            }
        }
        val buffer = planes[i].buffer
        val rowStride = planes[i].rowStride
        val pixelStride = planes[i].pixelStride
        val shift = if (i == 0) 0 else 1
        val w = width shr shift
        val h = height shr shift
        buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
        for (row in 0 until h) {
            var length: Int
            if (pixelStride == 1 && outputStride == 1) {
                length = w
                buffer[data, channelOffset, length]
                channelOffset += length
            } else {
                length = (w - 1) * pixelStride + 1
                buffer[rowData, 0, length]
                for (col in 0 until w) {
                    data[channelOffset] = rowData[col * pixelStride]
                    channelOffset += outputStride
                }
            }
            if (row < h - 1) {
                buffer.position(buffer.position() + rowStride - length)
            }
        }
    }
    return data
}


private fun processData(averageData: MutableList<Int>): Int {
    var dInt = 0
    var count = 0
    var isRise = false
    for (integer in averageData) {
        if (dInt == 0) {
            dInt = integer
            continue
        }
        if (integer > dInt) {
            if (!isRise) {
                count++
                isRise = true
            }
        } else {
            isRise = false
        }
        dInt = integer
    }
    return count
}

private fun vibrate() {
    var vibrator = HeartRateApp.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    vibrator?.cancel()
    vibrator?.vibrate(VibrationEffect.createOneShot(50, 255))
}