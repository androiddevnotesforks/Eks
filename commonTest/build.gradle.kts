plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ir.fallahpoor.eks.commontest"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":data"))
    api(libs.androidx.lifecycle.liveData)
    api(libs.kotlinx.coroutines.test)
    api(libs.junit)
    api(libs.androidx.core.testing)
    api(libs.androidx.test.core)
    api(libs.androidx.test.junit)
}