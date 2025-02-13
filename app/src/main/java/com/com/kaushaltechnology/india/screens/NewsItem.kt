package com.com.kaushaltechnology.india.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.com.kaushaltechnology.india.Utils
import com.com.kaushaltechnology.india.dao.gnews.Article
import com.com.kaushaltechnology.india.utils.TimeUtils
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsItem(article: Article, pagerState: PagerState) {
    val context = LocalContext.current
    val activity = context as? Activity
    var hasPermission by remember { mutableStateOf(checkPermissions(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions.values.all { it }
    }

   /* LaunchedEffect(Unit) {
        if (!hasPermission) {
            requestPermissions(context, permissionLauncher)
        }
    }*/

    Box(
        modifier = Modifier.fillMaxSize().padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(0.3f).padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE)),
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            article.urlToImage?.let { NewsCard(imageUrl = it) }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(20.dp), // Ensure the box has enough height
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        IconButton(
                            onClick = {
                                if (activity != null) {
                                    shareAppWithScreenshot(activity)
                                }
                            },
                            modifier = Modifier.size(48.dp) // Increase size to make it visible
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Share",tint = Color.White )
                        }
                    }


                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(0.5f).padding(16.dp), contentAlignment = Alignment.TopCenter) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${article.id} ${Utils.replaceSpecialChar(article.title)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = Utils.replaceSpecialChar(article.description),
                        fontSize = 20.sp,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(0.2f), contentAlignment = Alignment.TopCenter) {
                SourceAndTimeView(
                    source = article.source.name,
                    time = TimeUtils.formatDateTime(article.publishedAt),
                    pagerState
                )
            }
        }
    }
}

fun captureScreenshot(activity: Activity): Uri? {
    val view = activity.window.decorView.rootView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    val file = File(activity.externalCacheDir, "screenshot.png")
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }

    return FileProvider.getUriForFile(activity, "${activity.packageName}.provider", file)
}

fun shareAppWithScreenshot(activity: Activity) {
    val screenshotUri = captureScreenshot(activity)
    val appLink = "https://play.google.com/store/apps/details?id=com.aditechnology.tambola"

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: $appLink")
        type = "text/plain"
        screenshotUri?.let {
            putExtra(Intent.EXTRA_STREAM, it)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share via")
    activity.startActivity(shareIntent)
}

fun checkPermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}

fun requestPermissions(context: Context, launcher: androidx.activity.result.ActivityResultLauncher<Array<String>>) {
    val permissions = mutableListOf<String>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    launcher.launch(permissions.toTypedArray())
}
