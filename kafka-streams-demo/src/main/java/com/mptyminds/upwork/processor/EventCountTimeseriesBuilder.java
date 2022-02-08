package com.mptyminds.upwork.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;

/**
 * @author Abhishek Jha
 * @created 08/02/22
 */
public class EventCountTimeseriesBuilder {

    private static final String TIMESERIES_TOPIC = "wikimedia.stats.timeseries";
    private static final String TIMESERIES_STORE = "event-count-store";

    private final KStream<String, String> inputStream;

    public EventCountTimeseriesBuilder(KStream<String, String> inputStream) {
        this.inputStream = inputStream;
    }

    public void setup() {
        final TimeWindows timeWindows = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10));
        this.inputStream
                .selectKey((key, value) -> "key-to-group")
                .groupByKey()
                .windowedBy(timeWindows)
                .count(Materialized.as(TIMESERIES_STORE))
                .toStream()
                .to(TIMESERIES_TOPIC, Produced.with(
                        WindowedSerdes.timeWindowedSerdeFrom(String.class, timeWindows.size()),
                        Serdes.Long()
                ));
    }
}
