import java.net.URL

plugins {
    // Language Plugins
    `java-library`
    kotlin("jvm") version Plugins.kotlinVersion

    // Code Quality
    id("org.jlleitschuh.gradle.ktlint") version Plugins.ktLintVersion

    // Documentation Generation
    id("org.jetbrains.dokka") version Plugins.dokkaVersion

    // Maven Publication
    id("io.github.gradle-nexus.publish-plugin") version Plugins.nexusPublishVersion
    `maven-publish`
    signing
}

group = Coordinates.group
version = Coordinates.version

// What JVM version should this project compile to
val targetVersion = "1.8"
// What JVM version this project is written in
val sourceVersion = "1.8"

// Maven Repositories
repositories {
    mavenLocal()
    mavenCentral()
}

// Project Dependencies
dependencies {
    Dependencies.kotlinModules.forEach {
        implementation("org.jetbrains.kotlin:kotlin-$it:${Plugins.kotlinVersion}")
    }

    implementation(gradleApi())
}

// Disable unneeded rules
ktlint {
    this.disabledRules.add("no-wildcard-imports")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = targetVersion
    }

    compileJava {
        targetCompatibility = targetVersion
        sourceCompatibility = sourceVersion
    }

    dokkaHtml {
        val moduleFile = File(projectDir, "MODULE.temp.md")

        run {
            // In order to have a description on the rendered docs, we have to
            // have a file with the # Module thingy in it. That's what we're
            // automagically creating here.

            doFirst {
                moduleFile.writeText(
                    "# Module ${Coordinates.name}\n${Coordinates.desc}"
                )
            }

            doLast {
                moduleFile.delete()
            }
        }

        moduleName.set(Coordinates.name)

        dokkaSourceSets.configureEach {
            displayName.set("${Coordinates.name} github")
            includes.from(moduleFile.path)

            skipDeprecated.set(false)
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)
            suppressObviousFunctions.set(true)

            // Link the source to the documentation
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(
                    URL(
                        "https://github.com/${Coordinates.repoId}/tree/trunk/src"
                    )
                )
            }
        }
    }

    // The original artifact, we just have to add the LICENSE file.
    jar {
        from("LICENSE")
    }

    // Source artifact, including everything the 'main' does but not compiled.
    create("sourcesJar", Jar::class) {
        group = "build"

        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
        from("LICENSE")
    }

    // The Javadoc artifact, containing the Dokka output and the LICENSE file.
    create("javadocJar", Jar::class) {
        group = "build"

        val dokkaHtml = getByName("dokkaHtml")

        archiveClassifier.set("javadoc")
        dependsOn(dokkaHtml)
        from(dokkaHtml)

        from("LICENSE")
    }

    afterEvaluate {
        // Task priority
        val publishToSonatype by getting
        val closeAndReleaseSonatypeStagingRepository by getting

        closeAndReleaseSonatypeStagingRepository
            .mustRunAfter(publishToSonatype)

        // Wrapper task since calling both one after the other in IntelliJ
        // seems to cause some problems.
        create("releaseToSonatype") {
            group = "publishing"

            dependsOn(
                publishToSonatype,
                closeAndReleaseSonatypeStagingRepository
            )
        }
    }
}

// Define the default artifacts' tasks
val defaultArtifactTasks = arrayOf(
    tasks["sourcesJar"],
    tasks["javadocJar"]
)

// Declare the artifacts
artifacts {
    defaultArtifactTasks.forEach(::archives)
}

publishing.publications {
    // Sets up the Maven integration.
    create("mavenJava", MavenPublication::class.java) {
        from(components["java"])
        defaultArtifactTasks.forEach(::artifact)

        pom {
            name.set(Coordinates.name)
            description.set(Coordinates.desc)
            url.set("https://github.com/${Coordinates.repoId}")

            licenses {
                Pom.licenses.forEach {
                    license {
                        name.set(it.name)
                        url.set(it.url)
                        distribution.set(it.distribution)
                    }
                }
            }

            developers {
                Pom.developers.forEach {
                    developer {
                        id.set(it.id)
                        name.set(it.name)
                    }
                }
            }

            scm {
                val partialUrl = "github.com/${Coordinates.repoId}"

                connection.set("scm:git:git://$partialUrl.git")
                developerConnection.set("scm:git:ssh://$partialUrl.git")
                url.set("https://$partialUrl")
            }
        }

        // Configure the signing extension to sign this Maven artifact.
        signing {
            isRequired = project.properties["signing.keyId"] != null
            sign(this@create)
        }
    }
}

// Configure publishing to Maven Central
nexusPublishing.repositories.sonatype {
    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
    snapshotRepositoryUrl.set(
        uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    )

    // Skip this step if environment variables NEXUS_USERNAME or NEXUS_PASSWORD
    // aren't set.
    username.set(properties["NEXUS_USERNAME"] as? String ?: return@sonatype)
    password.set(properties["NEXUS_PASSWORD"] as? String ?: return@sonatype)
}
