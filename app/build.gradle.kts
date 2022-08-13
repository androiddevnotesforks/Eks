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
    compileSdk = SdkVersions.compileSdk

    defaultConfig {
        applicationId = "ir.fallahpoor.eks"
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.version
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes.add("**/attach_hotspot_windows.dll")
        resources.excludes.add("META-INF/licenses/ASM")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
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
    implementation(kotlinStdLib)
    implementation(appCompat)
    implementation(core)
    implementation(dataStore)
    implementation(activityCompose)
    implementation(constraintLayoutCompose)
    implementation(material)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.liveData)
    implementation(Lifecycle.viewModelCompose)
    implementation(Lifecycle.runtimeCompose)
    implementation(Room.runtime)
    implementation(WorkManager.workManager)
    implementation(timber)

    implementation(Hilt.android)
    implementation(Hilt.workManager)
    kapt(Hilt.androidCompiler)
    kapt(Hilt.compiler)

    implementation(Compose.ui)
    implementation(Compose.tooling)
    implementation(Compose.material)
    implementation(Compose.runtime)
    implementation(Compose.runtimeLiveData)

    testImplementation(project(":commonTest"))
    testImplementation(junit)
    testImplementation(truth)
    testImplementation(coreTesting)
    testImplementation(Coroutines.test)
    testImplementation(AndroidXTest.core)
    testImplementation(robolectric)
    testImplementation(Mockito.inline)

    androidTestImplementation(project(":commonTest"))
    androidTestImplementation(AndroidXTest.runner)
    androidTestImplementation(AndroidXTest.rules)
    androidTestImplementation(truth)
    androidTestImplementation(Compose.uiTestJunit)
    debugImplementation(Compose.uiTestManifest)
    androidTestImplementation(Mockito.core)
    androidTestImplementation(Mockito.dexMaker)
    androidTestImplementation(coreTesting)
    androidTestImplementation(Espresso.core)
    androidTestImplementation(Espresso.intents)
    androidTestImplementation(Hilt.androidTesting)
    androidTestImplementation(WorkManager.workManagerTest)
    kaptAndroidTest(Hilt.androidCompiler)
}