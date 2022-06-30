plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Constants.compileSdk
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = Constants.packageName
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk
        versionCode = Constants.versionCode
        versionName = Constants.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmVersion
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    namespace = "com.teambeme.beme"
}

dependencies {
    // Kotlin
    implementation(KotlinDependencies.kotlin)

    // AndroidX
    implementation(AndroidXDependencies.coreKtx)
    implementation(AndroidXDependencies.appCompat)
    implementation(AndroidXDependencies.constraintLayout)
    implementation(AndroidXDependencies.hilt)
    kapt(KaptDependencies.hiltCompiler)
    implementation(AndroidXDependencies.navigationFragment)
    implementation(AndroidXDependencies.fragment)
    implementation(AndroidXDependencies.legacy)
    implementation(AndroidXDependencies.security)
    implementation(AndroidXDependencies.coroutines)
    implementation(AndroidXDependencies.navigationUI)
    implementation(AndroidXDependencies.paging3)
    implementation(AndroidXDependencies.room)
    kapt(KaptDependencies.roomCompiler)
    implementation(AndroidXDependencies.roomKtx)

    // Material Design
    implementation(MaterialDesignDependencies.materialDesign)

    // Google
    implementation(ThirdPartyDependencies.playCore)
    implementation(platform(FirebaseDependency.firebaseBom))
    implementation(FirebaseDependency.analyticsKtx)
    implementation(FirebaseDependency.firebaeMessaging)

    // Third-Party
    implementation(ThirdPartyDependencies.circleImageView)
    implementation(ThirdPartyDependencies.glide)
    implementation(ThirdPartyDependencies.gson)
    implementation(ThirdPartyDependencies.imageCropper)
    implementation(ThirdPartyDependencies.lottie)
    implementation(ThirdPartyDependencies.okHttp)
    implementation(ThirdPartyDependencies.okHttpLogginInterceptor)
    implementation(ThirdPartyDependencies.tedPermission)
    implementation(ThirdPartyDependencies.stickyScrollView)
    implementation(ThirdPartyDependencies.viewPagerIndicator)
    implementation(ThirdPartyDependencies.retrofit)
    implementation(ThirdPartyDependencies.retrofitGsonConverter)
    implementation(ThirdPartyDependencies.pullRefreshLayout)

    // Test
    testImplementation(TestDependencies.jUnit)
    androidTestImplementation(TestDependencies.androidTest)
    androidTestImplementation(TestDependencies.espresso)
    testImplementation(TestDependencies.room)
}

ktlint {
    android.set(true)
    coloredOutput.set(true)
    verbose.set(true)
    outputToConsole.set(true)
}
