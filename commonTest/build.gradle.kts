plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "ir.fallahpoor.eks.commontest"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    // TODO remove it once the reason for this Lint error is found
    lint {
        disable += "DialogFragmentCallbacksDetector"
    }

}

dependencies {
    implementation(project(":data"))
    implementation(libs.lifecycle.liveData)
    implementation(libs.coroutines.test)
    implementation(libs.junit)
    implementation(libs.core.testing)
    implementation(libs.test.core)
    implementation(libs.test.junit)
}