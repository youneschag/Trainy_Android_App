plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.dagger)
}

android {
    namespace = "fr.uha.chaguer.trainy"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.uha.chaguer.trainy"
        minSdk = 33
        targetSdk = Integer.getInteger(libs.versions.sdk.get())
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_"+libs.versions.java.get())
        targetCompatibility = JavaVersion.valueOf("VERSION_"+libs.versions.java.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompose.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
    }
}

dependencies {
    // Main
    implementation(libs.androidx.core.ktx)
    implementation(kotlin("reflect"))
    api(libs.kolinx.coroutines.core)
    api(libs.kolinx.coroutines.android)

    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    //UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)

    // navigation
    implementation(libs.destinations.core)
    implementation(libs.firebase.vertexai)
    ksp(libs.destinations.ksp)

    // room
    implementation(libs.room.core)
    // required to allow Room to pick up classes in annotations from lib android
    implementation(project(mapOf("path" to ":android")))
    ksp(libs.room.ksp)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    implementation(libs.hilt.work)
    implementation(libs.work.runtime)
    ksp(libs.dagger.ksp)
    ksp(libs.hilt.ksp)

    // image
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}