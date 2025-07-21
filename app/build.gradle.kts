

plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.testingar"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.testingar"
        minSdk = 24
        targetSdk = 36 
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- ARCore Dependency ---

    implementation("com.google.ar:core:1.41.0")

    // --- Sceneform Dependency

    implementation("com.gorisse.thomas.sceneform:sceneform:1.21.0")


}