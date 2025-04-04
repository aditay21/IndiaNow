# Keep class names for model classes
-keep class com.kaushaltechnology.in.model.** { *; }

# Keep annotations
-keepattributes *Annotation*

# Prevent obfuscation of Retrofit and Gson model classes
-keep class com.kaushaltechnology.in.network.** { *; }
-keep class com.google.gson.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# Keep Hilt-generated classes
-keep class dagger.hilt.** { *; }
-keep class com.google.dagger.** { *; }
-dontwarn dagger.hilt.**

# Keep Room database entities and DAOs
-keep class androidx.room.** { *; }
-keep class com.kaushaltechnology.in.database.** { *; }

# Keep Glide classes
-keep class com.bumptech.glide.** { *; }
-keep class com.github.skydoves.landscapist.** { *; }

# Keep logging for debugging
-keep class okhttp3.logging.** { *; }

# Prevent stripping of Compose-related classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Parcelables
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
