import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
    annotationProcessor(springBoot("spring-boot-configuration-processor"))
    implementation(springBoot("spring-boot-starter-web"))
    implementation(springBoot("spring-boot-starter-log4j2"))
    implementation(springKafka())

		implementation(jackson())
    implementation(commonPool2())

    implementation(kotlinx("kotlinx-coroutines-core"))
    implementation(kotlinx("kotlinx-coroutines-reactor"))
    implementation(kotlinxSerialization())

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

		testImplementation(springBoot("spring-boot-starter-test"))
	}
}

tasks.getByName<BootJar>("bootJar") {
  enabled = false
}
