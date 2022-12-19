import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
	idea
	java
	kotlin("jvm") version Version.kotlin
	kotlin("kapt") version Version.kotlin
	kotlin("plugin.jpa") version Version.kotlin apply false
  kotlin(Plugin.kotlinSerialization) version Version.kotlin
	id(Plugin.kotlinSpring) version Version.kotlin apply false
	id(Plugin.springBoot) version Version.springBoot
	id(Plugin.springDependencyManagement) version Version.springDependencyManagement
  id(Plugin.ktlint) version Version.ktlintPlugin apply false
  id(Plugin.ktlintIdea) version Version.ktlintIdea
}

allprojects {
	group = "me.clud"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

tasks {
  register<Exec>("lint") {
    commandLine = "./gradlew ktlintCheck".split(" ")
  }
}

subprojects {
	apply(plugin = Plugin.idea)
	apply(plugin = Plugin.kotlin)
	apply(plugin = Plugin.kotlinSpring)
	apply(plugin = Plugin.springDependencyManagement)
	apply(plugin = Plugin.springBoot)
  apply(plugin = Plugin.ktlint)

	configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

  configurations {
    all {
      exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
  }

  tasks {
		compileKotlin {
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "17"
			}
		}

    check {
      dependsOn(getByName("ktlintCheck"))
    }
	}

  configure<KtlintExtension> {
    additionalEditorconfigFile.set(file("$rootDir/.editorconfig"))
  }

	dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.apache.commons:commons-pool2:2.11.1")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}
