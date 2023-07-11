import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Property

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("nu.studer.jooq") version "8.2.1"
}

group = "com.ttymonkey"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // kotlin and coroutines support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // r2dbc and postgresql
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    // flyway
    implementation("org.flywaydb:flyway-core:8.5.11")

    // jooq
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq-kotlin")
    jooqGenerator("org.jooq:jooq-meta-extensions")

    // test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc = null

                generator.apply {
                    database.apply {
                        name = "org.jooq.codegen.KotlinGenerator"
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"

                        properties.addAll(
                            listOf(
                                Property().apply {
                                    key = "scripts"
                                    value = "src/main/resources/db/migration"
                                },

                                Property().apply {
                                    key = "sort"
                                    value = "flyway"
                                },

                                Property().apply {
                                    key = "defaultNameCase"
                                    value = "lower"
                                },
                            ),
                        )
                    }
                    generate.apply {
                        isPojosAsKotlinDataClasses = true // use data classes
                        isKotlinNotNullPojoAttributes = true
                    }
                    target.apply {
                        packageName = "com.ttymonkey.springcoroutines.jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveFileName.set("server.jar")
}
