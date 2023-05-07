import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

val properties: java.util.Properties = gradleLocalProperties(rootDir)
val sp: String = properties.getProperty("storePassword")
val kp: String = properties.getProperty("keyPassword")

android {
    namespace = "ir.fallahpoor.eks"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ir.fallahpoor.eks"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "Eks")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../Eks.jks")
            storePassword = sp
            keyAlias = "key0"
            keyPassword = kp
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android.txt"
                ),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["release"]
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
        }
    }

    // TODO remove it once the reason for this Lint error is found
    lint {
        disable += "DialogFragmentCallbacksDetector"
    }
}

kapt {
    correctErrorTypes = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

dependencies {
    implementation(project(":data"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.appCompat)
    implementation(libs.core)
    implementation(libs.dataStore.preferences)
    implementation(libs.activity.compose)
    implementation(libs.constraintLayout.compose)
    implementation(libs.material)
    implementation(libs.bundles.lifecycle)
    implementation(libs.room.runtime)
    implementation(libs.workManager)
    implementation(libs.timber)

    implementation(libs.hilt.android)
    implementation(libs.hilt.workManager)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling)
    implementation(libs.compose.material)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.liveData)

    lintChecks(libs.composeLintChecks)

    testImplementation(project(":commonTest"))
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.core.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.turbine)

    androidTestImplementation(project(":commonTest"))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.compose.uiTest.junit)
    debugImplementation(libs.compose.uiTest.manifest)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.dexMaker)
    androidTestImplementation(libs.core.testing)
    androidTestImplementation(libs.bundles.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.workManager.test)
    kaptAndroidTest(libs.hilt.android.compiler)
}