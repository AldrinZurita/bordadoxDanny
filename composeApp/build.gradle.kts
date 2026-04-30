import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.util.Properties
import javax.inject.Inject
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.FileSystemOperations

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.work.runtime)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
        }
        commonMain.dependencies {
            implementation(project(":core:designsystem"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview) // Moved to commonMain
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.icons.extended)
            implementation(libs.navigation.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            
            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
    }
    sourceSets.commonTest.dependencies {
        implementation(kotlin("test"))
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "bo.bordadoxdanny.app"
    generateResClass = auto
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "bo.bordadoxdanny.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "bo.bordadoxdanny.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // REMOVED: implementation(project(":composeApp")) - This caused the circular dependency
    debugImplementation(libs.compose.uiTooling)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)

    // Firebase
    "androidMainImplementation"(platform(libs.firebase.bom))
    "androidMainImplementation"(libs.firebase.messaging)
    "androidMainImplementation"(libs.firebase.database)
    "androidMainImplementation"(libs.firebase.config)
}

// Fix for Circular Dependency in Compose Multiplatform Resources
// We use name-based lookup to avoid Unresolved reference: GenerateResValues
tasks.configureEach {
    if (name.contains("processDebugResources") || name.contains("processReleaseResources")) {
        mustRunAfter(tasks.matching { it.name.contains("generateComposeResValues", ignoreCase = true) })
    }
}

// Refactored task for Configuration Cache compatibility using injection
abstract class DownloadTranslationsTask @Inject constructor(
    private val fileSystem: FileSystemOperations,
    private val archiveOperations: ArchiveOperations
) : DefaultTask() {
    @get:Input
    abstract val apiKey: Property<String>

    @get:OutputDirectory
    abstract val targetResDir: DirectoryProperty

    @get:Internal
    abstract val buildDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val key = apiKey.get()
        if (key.isEmpty()) {
            println("Error: loco_api_key is empty or not found in local.properties")
            return
        }
        val zipUrl = "https://localise.biz/api/export/archive/xml.zip?key=$key&format=android"
        val tempZip = buildDir.file("loco_translations.zip").get().asFile
        val extractDir = buildDir.dir("loco_extracted").get().asFile
        val targetDir = targetResDir.get().asFile

        println("Downloading translations from Loco...")
        tempZip.parentFile.mkdirs()
        URI(zipUrl).toURL().openStream().use { input ->
            tempZip.outputStream().use { output -> input.copyTo(output) }
        }

        println("Extracting translations...")
        extractDir.deleteRecursively()
        fileSystem.copy {
            from(archiveOperations.zipTree(tempZip))
            into(extractDir)
        }

        val foundValuesDirs = extractDir.walkTopDown().filter { it.isDirectory && it.name.startsWith("values") }.toList()
        
        if (foundValuesDirs.isEmpty()) {
            println("Error: No 'values' folders found in the ZIP archive.")
            return
        }

        val mapping = mapOf(
            "values" to "values",
            "values-en-US" to "values-en",
            "values-en-rUS" to "values-en",
            "values-fr-FR" to "values-fr",
            "values-fr" to "values-fr"
        )

        foundValuesDirs.forEach { srcDir ->
            val stringsFile = File(srcDir, "strings.xml")
            if (stringsFile.exists()) {
                val folderName = srcDir.name
                val targetFolderName = mapping[folderName] ?: folderName
                
                val destDir = File(targetDir, targetFolderName)
                destDir.mkdirs()
                stringsFile.copyTo(File(destDir, "strings.xml"), overwrite = true)
                println("Updated: $targetFolderName/strings.xml (from $folderName)")
            }
        }
        
        tempZip.delete()
        extractDir.deleteRecursively()
        println("Loco integration complete.")
    }
}

tasks.register<DownloadTranslationsTask>("downloadTranslations") {
    group = "localization"
    description = "Downloads and extracts translations from Loco (localize.biz)"
    
    val props = Properties().apply {
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) load(localPropsFile.inputStream())
    }
    
    apiKey.set(props.getProperty("loco_api_key") ?: "")
    targetResDir.set(layout.projectDirectory.dir("src/commonMain/composeResources"))
    buildDir.set(layout.buildDirectory)
}
