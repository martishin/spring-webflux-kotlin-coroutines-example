package com.ttymonkey.springcoroutines.configs

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy

@Configuration
class JooqConfig(private val connectionFactory: ConnectionFactory) {

    @Bean
    fun dslContext(): DSLContext {
        return DSL.using(TransactionAwareConnectionFactoryProxy(connectionFactory), SQLDialect.POSTGRES)
    }
}
