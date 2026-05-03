plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "bo.bordadoxdanny.app.core.designsystem"
        compileSdk = 35
        minSdk = 24
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DesignSystem"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
        }

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "bo.bordadoxdanny.app.core.designsystem"
    generateResClass = auto
}
