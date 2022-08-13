plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "ir.fallahpoor.eks.commontest"
    compileSdk = SdkVersions.compileSdk

    defaultConfig {
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk

        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

dependencies {
    implementation(project(":data"))
    implementation(Lifecycle.liveData)
    implementation(Coroutines.test)
    implementation(junit)
    implementation(coreTesting)
    implementation(AndroidXTest.core)
    implementation(AndroidXTest.junit)
}