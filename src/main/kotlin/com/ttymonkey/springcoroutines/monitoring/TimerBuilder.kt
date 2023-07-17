package com.ttymonkey.springcoroutines.monitoring

import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import java.time.Duration

internal object TimerBuilder {

    fun statisticTimerBuilder(
        metricsLabelTag: String,
        statusTag: String? = null,
        moreTags: Map<String, String>,
        timeBuckets: Array<Duration>,
    ): Timer.Builder = Timer.builder("$metricsLabelTag.statistic")
        .tags(
            mutableListOf(
                Tag.of("service", metricsLabelTag),
            ).apply {
                statusTag?.let {
                    add(Tag.of("status", it))
                }
                addAll(moreTags.map { Tag.of(it.key, it.value) })
            },
        )
        .distributionStatisticBufferLength(DefaultConstants.BUFFER_LENGTH)
        .serviceLevelObjectives(*timeBuckets)
        .distributionStatisticExpiry(Duration.ofMinutes(1))
        .publishPercentiles(
            DefaultConstants.FIFTIETH_PERCENTILE,
            DefaultConstants.NINETIETH_PERCENTILE,
            DefaultConstants.NINETY_NINTH_PERCENTILE,
        )
        .description("Elapsed duration for execution")
}
