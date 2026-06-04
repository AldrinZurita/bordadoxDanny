import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.util.Properties
import javax.inject.Inject
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.FileSystemOperations
import java.io.File

// Imports para leer y fusionar XML
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Element
import org.w3c.dom.Node

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
            implementation(project(":core:daemon"))
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.work.runtime)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
            implementation(libs.compose.uiToolingPreview)
        }
        commonMain.dependencies {
            implementation(project(":core:designsystem"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
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

tasks.configureEach {
    if (name.contains("processDebugResources") || name.contains("processReleaseResources")) {
        mustRunAfter(tasks.matching { it.name.contains("generateComposeResValues", ignoreCase = true) })
    }
}

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

        // MAPEO ACTUALIZADO PARA FORZAR EL MERGE EN TUS CARPETAS ACTUALES
        val mapping = mapOf(
            // Loco exporta el idioma base como 'values'.
            // Si quieres que tu inglés base actualice 'values-en', cambia el valor de la derecha.
            "values" to "values",

            // Variantes de Inglés USA
            "values-en" to "values",
            "values-en-US" to "values",
            "values-en-rUS" to "values",
            "values-en_US" to "values",

            // Variantes para Bolivia (tu carpeta values-es-rBO)
            "values-en-BO" to "values-es-rBO",
            "values-en-rBO" to "values-es-rBO",
            "values-es-BO" to "values-es-rBO",
            "values-es-rBO" to "values-es-rBO",

            // Francés
            "values-fr" to "values-fr-rFR",
            "values-fr-FR" to "values-fr-rFR",
            "values-fr-rFR" to "values-fr-rFR"
        )

        foundValuesDirs.forEach { srcDir ->
            val stringsFile = File(srcDir, "strings.xml")
            if (stringsFile.exists()) {
                val folderName = srcDir.name
                val targetFolderName = mapping[folderName] ?: folderName

                // LOG DE DIAGNÓSTICO: Esto te dirá qué está pasando en la consola
                println("INFO: Carpeta en ZIP: '$folderName' -> Mapeada a destino: '$targetFolderName'")

                val destDir = File(targetDir, targetFolderName)
                destDir.mkdirs()
                val destFile = File(destDir, "strings.xml")

                if (destFile.exists()) {
                    mergeXmlFiles(destFile, stringsFile)
                    println("SUCCESS: Merge completado en $targetFolderName/strings.xml")
                } else {
                    stringsFile.copyTo(destFile, overwrite = true)
                    println("SUCCESS: Archivo nuevo creado en $targetFolderName/strings.xml")
                }
            }
        }

        tempZip.delete()
        extractDir.deleteRecursively()
        println("Loco integration complete.")
    }

    private fun mergeXmlFiles(existingFile: File, newFile: File) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()

            val existingDoc = builder.parse(existingFile)
            val newDoc = builder.parse(newFile)

            val existingResources = existingDoc.getElementsByTagName("resources").item(0) as? Element ?: return
            val newResources = newDoc.getElementsByTagName("resources").item(0) as? Element ?: return

            val newNodes = newResources.childNodes
            for (i in 0 until newNodes.length) {
                val node = newNodes.item(i)
                if (node.nodeType == Node.ELEMENT_NODE) {
                    val element = node as Element
                    val name = element.getAttribute("name")

                    val existingNodes = existingResources.childNodes
                    var found = false

                    for (j in 0 until existingNodes.length) {
                        val exNode = existingNodes.item(j)
                        if (exNode.nodeType == Node.ELEMENT_NODE) {
                            val exElement = exNode as Element
                            if (exElement.tagName == element.tagName && exElement.getAttribute("name") == name) {
                                val importedNode = existingDoc.importNode(element, true)
                                existingResources.replaceChild(importedNode, exElement)
                                found = true
                                break
                            }
                        }
                    }

                    if (!found) {
                        val importedNode = existingDoc.importNode(element, true)
                        existingResources.appendChild(importedNode)
                    }
                }
            }

            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
            transformer.transform(DOMSource(existingDoc), StreamResult(existingFile))

        } catch (e: Exception) {
            println("ERROR en merge de XML: ${e.message}")
        }
    }
}

tasks.register<DownloadTranslationsTask>("downloadTranslations") {
    group = "localization"
    description = "Downloads and merges translations from Loco"

    val props = Properties().apply {
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) load(localPropsFile.inputStream())
    }

    apiKey.set(props.getProperty("loco_api_key") ?: "")
    targetResDir.set(layout.projectDirectory.dir("src/commonMain/composeResources"))
    buildDir.set(layout.buildDirectory)
}