plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.moutamid.peptidesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.moutamid.peptidesapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "PeptidesApp-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { viewBinding = true }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.fxn769:stash:1.3.2")
    implementation("com.zhouyou:signseekbar:1.0.6")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}