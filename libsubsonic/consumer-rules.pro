# Keep all public data classes used by library consumers.
# These are returned from SubsonicClient methods and must not be obfuscated or removed.
-keep class com.vsulimov.libsubsonic.SubsonicClient { *; }
-keep class com.vsulimov.libsubsonic.data.** { *; }
-keep class com.vsulimov.libsubsonic.http.HttpClient { *; }
