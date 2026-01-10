plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.vsulimov.libsubsonic"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        lint.targetSdk = 36
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.httpclient)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.json)
}

publishing {
    publications {
        afterEvaluate {
            create<MavenPublication>("release") {
                groupId = "com.vsulimov"
                artifactId = "libsubsonic"
                version = "1.0.0"

                from(components["release"])

                pom {
                    packaging = "aar"
                    name = "Subsonic"
                    description = "A lightweight Android library for interacting with the Subsonic API."
                    licenses {
                        license {
                            name = "The MIT License (MIT)"
                            url = "https://mit-license.org/"
                        }
                    }
                    developers {
                        developer {
                            name = "Vitaly Sulimov"
                            email = "v.sulimov.dev@imap.cc"
                        }
                    }
                    scm {
                        url = "https://git.vsulimov.com/android-libsubsonic.git"
                    }
                }
            }
        }
    }
}
