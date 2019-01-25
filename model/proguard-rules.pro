# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class url to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file url.
#-renamesourcefileattribute SourceFile

# 协议dto与vo类还有local不做混淆
-keep class com.beecampus.model.dto.** {*;}
-keep class com.beecampus.model.local.** {*;}
-keep class com.beecampus.model.remote.** {*;}
-keep class com.beecampus.model.vo.** {*;}