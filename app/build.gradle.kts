import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
}

val props = Properties()
val localPropertiesFile = File(rootProject.rootDir, "local.properties")
if (localPropertiesFile.exists()) {
    props.load(FileInputStream(localPropertiesFile))
} else {
    throw GradleException("local.properties file not found.")
}
val sp: String = props.getProperty("storePassword")
val kp: String = props.getProperty("keyPassword")

android {
    namespace = "ir.fallahpoor.eks"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ir.fallahpoor.eks"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.1"
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kapt {
    correctErrorTypes = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
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
    implementation(libs.immutableCollections)

    implementation(libs.hilt.android)
    implementation(libs.hilt.workManager)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling)
    implementation(libs.compose.material3)
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