plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "0.6.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(
        kotlin(
            module = "stdlib-jdk7",
            version = org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION
        )
    )

    val coroutinesVersion = "1.4.1"
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    )
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    )

    val archVersion = "2.3.0"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$archVersion")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$archVersion")
    implementation ("androidx.lifecycle:lifecycle-common-java8:$archVersion")

    implementation(project(":domain"))
    implementation(project(":common"))

    // ffmpeg
    implementation("com.arthenica:mobile-ffmpeg-full-gpl:4.4.LTS")
}