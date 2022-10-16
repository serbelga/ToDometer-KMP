@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    alias(libs.plugins.composeMultiplatform)
    id("com.android.library")
    id("todometer.spotless")
}

group = "dev.sergiobelda.todometer.common.compose.ui"
version = "1.0"

repositories {
    google()
}

kotlin {
    android()
    jvm("desktop")
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                // TODO: Remove material dependency when everything depend on material3.
                api(compose.material)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.ui)

                implementation(projects.common.domain)
                implementation(projects.common.composeUiDesignsystem)
                implementation(projects.common.ui)

                implementation(libs.kotlin.datetime)
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                // TODO: Remove this usage when compose.material3 reaches Material3 Compose 1.0.0-beta01.
                api(libs.androidx.compose.material3)
                api(compose.uiTooling)
            }
        }
        val androidTest by getting
        val desktopMain by getting {
            dependencies {
                api(libs.kotlin.coroutinesSwing)
                api(compose.uiTooling)
            }
        }
        val desktopTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

android {
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
    }
    namespace = "dev.sergiobelda.todometer.common.compose.ui"
}
