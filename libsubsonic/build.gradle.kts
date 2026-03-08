plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.vsulimov.libsubsonic"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        lint.targetSdk = 36
        consumerProguardFiles("consumer-rules.pro")
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
        jvmToolchain(jdkVersion = 11)
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.json)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        afterEvaluate {
            create<MavenPublication>("release") {
                groupId = "com.vsulimov"
                artifactId = "libsubsonic"
                version = "1.0.0-rc1"

                from(components["release"])

                pom {
                    packaging = "aar"
                    name = "libsubsonic"
                    description = "A lightweight Android library for interacting with the Subsonic API."
                    url = "https://github.com/v-sulimov/android-libsubsonic"
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
                        url = "https://github.com/v-sulimov/android-libsubsonic"
                        connection = "scm:git:https://github.com/v-sulimov/android-libsubsonic.git"
                        developerConnection = "scm:git:ssh://git@github.com/v-sulimov/android-libsubsonic.git"
                    }
                }
            }
        }
    }
}
