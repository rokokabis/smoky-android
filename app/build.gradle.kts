plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.rokokabis.smoky"
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
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.android.support.constraint:constraint-layout:2.0.4")

    // dependency injection
    //val koinVersion = "1.0.0"
    implementation("org.koin:koin-android:2.1.5")
    implementation("org.koin:koin-android-scope:2.1.5")
    implementation("org.koin:koin-android-viewmodel:2.1.5")

    // android nav components
    val navVersion = "2.3.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // exo-player
    val exoVersion = "2.13.2"
    implementation("com.google.android.exoplayer:exoplayer-core:$exoVersion")
    implementation("com.google.android.exoplayer:exoplayer-dash:$exoVersion")
    implementation("com.google.android.exoplayer:exoplayer-hls:$exoVersion")
    implementation("com.google.android.exoplayer:exoplayer-smoothstreaming:$exoVersion")
    implementation("com.google.android.exoplayer:exoplayer-ui:$exoVersion")

    // permission module
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    // ffmpeg
    implementation("com.arthenica:mobile-ffmpeg-full-gpl:4.4.LTS")

    // glide
    val glideVersion = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("androidx.test.ext:truth:1.3.0")
    testImplementation("org.mockito:mockito-core:3.1.0")

    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}