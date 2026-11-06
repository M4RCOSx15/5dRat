plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.fivedrat"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.fivedrat"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    buildFeatures {
        compose = true
    }

    lint {
        // local.properties e gerado por maquina (sdk.dir) e fora do VCS;
        // o check PropertyEscape entra em falso positivo no Windows.
        disable += "PropertyEscape"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// O plugin compose solicita compose-group-mapping:2.2.10 (inexistente nos
// repos); força a 2.4.21, casada com o Kotlin do projeto (ADR-005).
configurations.matching { it.name.contains("composeMappingProducerClasspath") }.configureEach {
    resolutionStrategy.force("org.jetbrains.kotlin:compose-group-mapping:2.4.21")
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation(libs.junit)
    // Spike M1c — JDBC apenas em teste JVM; o motor real usa android.database.sqlite
    testImplementation(libs.sqlite.jdbc)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
