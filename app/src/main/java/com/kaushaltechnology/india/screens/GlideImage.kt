package com.kaushaltechnology.india.screens


import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun GlideImage(imageUrl: String, thumbnailResId: Int) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(), // Full screen support
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageView ->
            Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions().centerCrop()) // Ensures full-size scaling
                .thumbnail(Glide.with(context).load(thumbnailResId)) // Load thumbnail first
                .into(imageView)
        }
    )
}
//https://images.icc-cricket.com/image/upload/t_ratio16_9-size40/prd/dnk795hdoabawciobbzj
