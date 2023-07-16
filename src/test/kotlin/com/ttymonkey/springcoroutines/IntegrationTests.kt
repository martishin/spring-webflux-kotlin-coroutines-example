package com.ttymonkey.springcoroutines

import com.ttymonkey.springcoroutines.dto.CompanyRequest
import com.ttymonkey.springcoroutines.dto.CompanyResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForObject
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@Testcontainers
class IntegrationTests(
    @Autowired val client: TestRestTemplate,
    @Autowired val databaseClient: DatabaseClient,
) {
    companion object {
        @Container
        val container = PostgreSQLContainer("postgres:15.3-alpine")
            .withDatabaseName("monkey")
            .withUsername("postgres")
            .withPassword("password")

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") {
                "r2dbc:postgresql://${container.host}:${container.getMappedPort(5432)}/${container.databaseName}"
            }
            registry.add("spring.r2dbc.username", container::getUsername)
            registry.add("spring.r2dbc.password", container::getPassword)

            registry.add("spring.flyway.url") {
                "jdbc:postgresql://${container.host}:${container.getMappedPort(5432)}/${container.databaseName}"
            }
            registry.add("spring.flyway.username", container::getUsername)
            registry.add("spring.flyway.password", container::getPassword)
        }
    }

    @AfterEach
    fun cleanup() {
        databaseClient.sql("TRUNCATE TABLE companies")
            .then()
            .subscribe()
    }

    @Test
    fun `testing if company can be created`() {
        // given
        val name = UUID.randomUUID().toString()
        val address = UUID.randomUUID().toString()

        val companyRequest = CompanyRequest(name, address)

        // when
        val id = client.postForObject<CompanyResponse>("/api/companies", companyRequest)!!.id

        // then
        val companyResponse = client.getForObject<CompanyResponse>("/api/companies/$id")!!

        assertThat(companyResponse.id).isEqualTo(id)
        assertThat(companyResponse.name).isEqualTo(name)
        assertThat(companyResponse.address).isEqualTo(address)
    }
}
