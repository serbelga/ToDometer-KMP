plugins {
    `kotlin-dsl`
}

group = "dev.sergiobelda.todometer.buildlogic"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
    implementation(libs.dependencyGraphGenerator)
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.xmlpull)
    implementation(libs.xpp3)
}

gradlePlugin {
    plugins {
        register("commonLibraryAndroid") {
            id = "todometer.common.library.android"
            implementationClass = "dev.sergiobelda.todometer.convention.CommonLibraryAndroidConventionPlugin"
        }
        register("dependencyGraphGenerator") {
            id = "todometer.dependency-graph-generator"
            implementationClass = "dev.sergiobelda.todometer.convention.DependencyGraphGeneratorConventionPlugin"
        }
        register("spotless") {
            id = "todometer.spotless"
            implementationClass = "dev.sergiobelda.todometer.convention.SpotlessConventionPlugin"
        }
        register("generateImages") {
            id = "todometer.common.generateImages"
            implementationClass = "dev.sergiobelda.todometer.convention.ImageVectorGenerationPlugin"
        }
    }
}
