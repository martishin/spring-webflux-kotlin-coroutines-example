package com.ttymonkey.springcoroutines.repositories

import com.ttymonkey.springcoroutines.jooq.application.Tables.COMPANIES
import com.ttymonkey.springcoroutines.models.Company
import com.ttymonkey.springcoroutines.toCompany
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.Configuration
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

suspend fun <T : Any> transactionWithSingleResult(
    dslContext: DSLContext,
    transactional: (Configuration) -> Flow<T>,
): T = Flux.from(dslContext.transactionPublisher { trx -> transactional.invoke(trx).asFlux() }).awaitSingle()

@Repository
class DefaultCompanyRepository(private val dslContext: DSLContext) : CompanyRepository {
    override fun findByNameContaining(name: String): Flow<Company> {
        val sql = dslContext.selectFrom(COMPANIES)
            .where(COMPANIES.NAME.like("%$name%"))

        return Flux.from(sql)
            .map { it.toCompany() }
            .asFlow()
    }

    override fun findAll(): Flow<Company> {
        val sql = dslContext.selectFrom(COMPANIES)

        return Flux.from(sql)
            .map { it.toCompany() }
            .asFlow()
    }

    override suspend fun findById(id: Int): Company? {
        return findById(id, this.dslContext)
    }

    override suspend fun deleteById(id: Int): Int {
        val sql = dslContext.deleteFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))

        return Mono.from(sql)
            .awaitSingle()
    }

    override suspend fun save(company: Company): Company? {
        return transactionWithSingleResult(dslContext) { trx ->
            trx.dsl()
                .insertInto(COMPANIES)
                .columns(COMPANIES.NAME, COMPANIES.ADDRESS)
                .values(company.name, company.address)
                .returningResult(COMPANIES.ID)
                .asFlow()
                .map { findById(it.value1(), trx.dsl())!! }
        }
    }

    private suspend fun findById(
        id: Int,
        dslContext: DSLContext,
    ): Company? {
        val sql = dslContext.selectFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))

        return Mono.from(sql)
            .map { it.toCompany() }
            .awaitSingleOrNull()
    }
}
