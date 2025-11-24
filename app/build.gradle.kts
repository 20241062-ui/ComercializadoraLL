plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")

}

android {
    namespace = "com.example.comercializadorall"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.comercializadorall"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:5.0.5")
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

// Keep these (and ensure they use the latest version)
    implementation("androidx.media3:media3-exoplayer:1.8.0")
    implementation("androidx.media3:media3-ui:1.8.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.8.0")
    implementation("androidx.media3:media3-extractor:1.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // PRUEBAS DE INSTRUMENTACIÓN (androidTestImplementation)
    // Framework de pruebas principal
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:rules:1.7.0")

    // Espresso para interactuar con la UI
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // MockWebServer: CLAVE para simular el servidor de la API
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")

    // Si usas Hilt para inyección de dependencias
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    // ... y el procesador de anotaciones
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.57.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0") // Ayuda con sintaxis Kotlin
    testImplementation(kotlin("test"))
}