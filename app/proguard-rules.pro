# This is a configuration file for ProGuard.
# https://www.guardsquare.com/en/proguard/manual/introduction

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Programs\Tools\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-keep class com.andrioussolutions.admob.** { *; }

-keep class com.andrioussolutions.ui.SpinnerObj { *; }

-keep class com.andrioussolutions.frmwrk.App { *; }
-keep class com.andrioussolutions.frmwrk.settings.appSettings { *; }
-keep class com.andrioussolutions.frmwrk.appModel { *; }
-keep class com.andrioussolutions.frmwrk.appModel$dataItem { *; }
-keep class com.andrioussolutions.frmwrk.db.dbDataRecords { *; }
-keep class com.andrioussolutions.frmwrk.db.dbHelper { *; }
-keep class com.andrioussolutions.frmwrk.appController { *; }
-keep class com.andrioussolutions.frmwrk.appView{ *; }
-keep class com.andrioussolutions.frmwrk.appAnalytics { *; }
-keep class com.andrioussolutions.frmwrk.settings.appPreferences { *; }
-keep class com.andrioussolutions.frmwrk.settings.appPreferences$OnDestroyListener { *; }
-keep class com.andrioussolutions.frmwrk.appActivity { *; }
-keep class com.andrioussolutions.frmwrk.appActivity$OnCreateViewListener { *; }
-keep class com.andrioussolutions.frmwrk.appActivity$OnBackPressedListener { *; }
-keep class com.andrioussolutions.frmwrk.settings.CheckBoxPreference { *; }
-keep class com.andrioussolutions.frmwrk.appFireBaseDB { *; }
-keep class com.andrioussolutions.frmwrk.settings.abListPreference { *; }
-keep class com.andrioussolutions.frmwrk.ui.appNotifications { *; }
-keep class com.andrioussolutions.frmwrk.files.appChangeLog { *; }
-keep class com.andrioussolutions.frmwrk.files.appChangeLog$Listmode { *; }
-keep class com.andrioussolutions.frmwrk.message.appChatLine { *; }
-keep class com.andrioussolutions.frmwrk.message.appMessaging{ *; }
-keep class com.andrioussolutions.frmwrk.message.appMessaging$DataMessageListener { *; }
-keep class com.andrioussolutions.frmwrk.message.appMessaging$NotificationListener { *; }
#-keep class com.andrioussolutions.frmwrk.** { *; }



-keep class com.andrioussolutions.conn.HttpConn { *; }

-keep class com.andrioussolutions.utils.Intl { *; }
-keep class com.andrioussolutions.utils.DoubleUtils { *; }
-keep class com.andrioussolutions.utils.dialog { *; }
-keep class com.andrioussolutions.utils.dialog$DialogBoxListener { *; }
-keep class com.andrioussolutions.utils.uri { *; }
-keep class com.andrioussolutions.utils.savedInstanceState{ *; }
-keep class com.andrioussolutions.utils.OrientateListener{ *; }

-keep class com.andrioussolutions.files.ViewLog { *; }

-keep class com.google.firebase.** { *; }

#-keepattributes InnerClasses

# use this option to remove logging code.
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# It's being sought but doesn't need to be.
-dontwarn org.joda.convert.**

# It's included as a jar file so don't worry, right?
#-dontwarn com.example.exceptionhandler.**

# Necessary since adding compile files('../../../libs/opencsv/opencsv-3.3.jar')
-dontwarn org.apache.commons.collections.BeanMap
-dontwarn java.beans.**

##---------------Begin: proguard configuration common for all Android apps ----------

# Try a little more passes for kicks.
-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

#To safely unpack jars on Windows. Obfuscated jars will become slightly larger.
#-dontusemixedcaseclassnames
#Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting.
-dontskipnonpubliclibraryclasses
#Don't ignore package visible library class members (fields and methods).
-dontskipnonpubliclibraryclassmembers
# Only when eventually targeting Android, it is not necessary, so you can then switch it off to reduce the processing time a bit.
-dontpreverify
# If there's an exception, this will print out a stack trace, instead of just the exception message.
 -verbose
 # This can improve the results of the optimization step.
 # Only applicable when obfuscating with the -repackageclasses option.
 -allowaccessmodification
 ## Removes package names making the code even smaller and less comprehensible.
 -repackageclasses ''


#Describes the internal structure of all the class files in the APK.
# dump.txt
-dump class_files.txt
#Lists the classes and members that were not obfuscated.
-printseeds seeds.txt
-printusage unused.txt
# Provides a translation between the original and obfuscated class, method, and field names.
-printmapping mapping.txt


##------------- Lets obfuscated code produce stack traces that can still be deciphered later on
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable


# Keep because these classes can be declared in the AndrodiManifest.xml.
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver

#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgent
#-keep public class * extends android.app.backup.BackupAgentHelper

#-keep public class * extends android.preference.Preference

-keep public class com.android.vending.licensing.ILicensingService{ *; }
-dontnote com.android.vending.licensing.ILicensingService

#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.support.v4.app.DialogFragment

#-keep class com.google.android.gms.internal.** { *; }
#-keep class com.google.android.gms.dynamic.** { *; }
#-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.android.gms.** { *; }

#-keep class com.google.android.gms.ads.AdListener{ *; }
#-keep class com.google.android.gms.ads.AdRequest { *; }
#-keep class com.google.android.gms.maps.** { *; }
#-keep interface com.google.android.gms.maps.** { *; }

-keep class com.google.ads.** { *; }


# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
# Preserve all native method names and the names of their classes.
-keepclasseswithmembers class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

 ##---------------Keep custom views  since they are probably referenced only from layout XML instead of application code

#-keepclassmembers !abstract class !com.google.ads.** extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
#-keepclassmembers !abstract class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.content.Context {
#   public void *(android.view.View);
#}
###-----------------  End of Keep Custom Views

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

## Saves any public class    YOU THEN DON'T OBFUSCATE ANYTHING??
#-keep public class * {
#    public protected *;
#}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keepclassmembers class * implements android.os.Parcelable {
#    static *** CREATOR;
#    }

 ##---------------End: proguard configuration common for all Android apps

##--------------- Understand the @Keep support annotation.
-keepattributes *Annotation*

-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

##---------------Begin: proguard configuration for support library
#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep class com.actionbarsherlock.** { *; }
#-keep interface com.actionbarsherlock.** { *; }
#
## The support library contains references to newer platform versions.
## Don't warn about those in case this app is linking against an older
## platform version. We know about them, and they are safe.
#-dontwarn android.support.**
#-dontwarn com.google.ads.**
###---------------End: proguard configuration for support library


###---------------Begin: proguard configuration for Gson
## Gson uses generic type information stored in a class file when working with fields. Proguard
## removes such information by default, so configure it to keep all of it.
#-keepattributes Signature
#
## For using GSON @Expose annotation
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#
## Application classes that will be serialized/deserialized over Gson
#-keep class com.example.model.** { *; }
###---------------End: proguard configuration for Gson  ----------

# If your project uses WebView with JS, uncomment the following
 #and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
