package com.kaushaltechnology.india.screens


import android.graphics.Color
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.com.kaushaltechnology.`in`.R

@Composable
fun GlideImage(imageUrl: String) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(), // Full screen support
        factory = { ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                setBackgroundColor(Color.TRANSPARENT)
            }
        },
        update = { imageView ->
            Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions().centerCrop().error(R.drawable.no_bg_img)) // Ensures full-size scaling
                .thumbnail(Glide.with(context).load(R.drawable.loading_bar)) // Load thumbnail first
                .into(imageView)
        }
    )
}
//https://images.icc-cricket.com/image/upload/t_ratio16_9-size40/prd/dnk795hdoabawciobbzj
