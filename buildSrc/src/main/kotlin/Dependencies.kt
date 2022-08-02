const val kotlinVersion = "1.7.0"

const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
const val core = "androidx.core:core-ktx:1.8.0"
const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"
const val appCompat = "androidx.appcompat:appcompat:1.4.2"
const val activityCompose = "androidx.activity:activity-compose:1.5.1"
const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
const val material = "com.google.android.material:material:1.4.0"
const val timber = "com.jakewharton.timber:timber:4.7.1"
const val inject = "javax.inject:javax.inject:1"
const val junit = "junit:junit:4.13.2"
const val truth = "com.google.truth:truth:1.1.3"
const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
const val robolectric = "org.robolectric:robolectric:4.8.1"
const val jsoup = "org.jsoup:jsoup:1.14.2"

object Lifecycle {
    private const val lifecycleVersion = "2.5.1"
    const val liveData =
        "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
    const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1"
}

object Room {
    private const val version = "2.4.3"
    const val runtime = "androidx.room:room-runtime:$version"
    const val ktx = "androidx.room:room-ktx:$version"
    const val compiler = "androidx.room:room-compiler:$version"
}

object WorkManager {
    private const val version = "2.7.1"
    const val workManager = "androidx.work:work-runtime-ktx:$version"
    const val workManagerTest = "androidx.work:work-testing:$version"
}

object Compose {
    const val version = "1.2.0"
    const val ui = "androidx.compose.ui:ui:$version"
    const val tooling = "androidx.compose.ui:ui-tooling:$version"
    const val material = "androidx.compose.material:material:$version"
    const val runtime = "androidx.compose.runtime:runtime:$version"
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata:$version"
    const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
}

object Coroutines {
    private const val version = "1.6.0"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
}

object Hilt {
    private const val jetpackVersion = "1.0.0"
    const val version = "2.43"
    const val android = "com.google.dagger:hilt-android:$version"
    const val androidCompiler =
        "com.google.dagger:hilt-android-compiler:$version"
    const val compiler = "androidx.hilt:hilt-compiler:$jetpackVersion"
    const val workManager = "androidx.hilt:hilt-work:$jetpackVersion"
    const val androidTesting = "com.google.dagger:hilt-android-testing:${version}"
}

object AndroidXTest {
    const val core = "androidx.test:core:1.0.0"
    const val runner = "androidx.test:runner:1.1.0"
    const val rules = "androidx.test:rules:1.1.0"
}

object Espresso {
    private const val version = "3.4.0"
    const val core = "androidx.test.espresso:espresso-core:$version"
    const val intents = "androidx.test.espresso:espresso-intents:$version"
}

object Mockito {
    private const val version = "4.3.1"
    const val core = "org.mockito:mockito-core:$version"
    const val inline = "org.mockito:mockito-inline:$version"
    const val dexMaker = "com.linkedin.dexmaker:dexmaker-mockito-inline:2.25.1"
}