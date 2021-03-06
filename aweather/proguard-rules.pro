# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/dema/android/android-studio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class mCityName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn org.apache.**
-dontwarn com.squareup.**
-dontwarn okio.**
-keep class im.dema.aweather.Models.CurrentWeatherModel { *; }

-keep class io.realm.** { *; }
-dontwarn javax.**
-dontwarn io.realm.**
