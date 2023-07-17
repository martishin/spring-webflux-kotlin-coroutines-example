package com.ttymonkey.springcoroutines.monitoring

import java.time.Duration

object DefaultConstants {
    // Metrics Constants: statistics and histogram
    val DEFAULT_TIME_BUCKETS = arrayOf(
        Duration.ofMillis(5),
        Duration.ofMillis(10),
        Duration.ofMillis(15),
        Duration.ofMillis(20),
        Duration.ofMillis(25),
        Duration.ofMillis(30),
        Duration.ofMillis(35),
        Duration.ofMillis(40),
        Duration.ofMillis(45),
        Duration.ofMillis(50),
        Duration.ofMillis(60),
        Duration.ofMillis(70),
        Duration.ofMillis(80),
        Duration.ofMillis(90),
        Duration.ofMillis(100),
        Duration.ofMillis(125),
        Duration.ofMillis(150),
        Duration.ofMillis(175),
        Duration.ofMillis(200),
        Duration.ofMillis(250),
        Duration.ofMillis(300),
        Duration.ofMillis(350),
        Duration.ofMillis(400),
        Duration.ofMillis(450),
        Duration.ofMillis(500),
        Duration.ofMillis(600),
        Duration.ofMillis(700),
        Duration.ofMillis(800),
        Duration.ofMillis(9000),
        Duration.ofMillis(1000),
        Duration.ofMillis(1250),
        Duration.ofMillis(1500),
    )

    const val FIFTIETH_PERCENTILE = 0.5
    const val NINETIETH_PERCENTILE = 0.9
    const val NINETY_NINTH_PERCENTILE = 0.99
    const val BUFFER_LENGTH = 3
}
