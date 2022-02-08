## PRE-REQUISITES
1. Kafka server setup
2. 2 Topics
3. Serde config on console consumers


### View Consumer O/p using
```
bin/kafka-console-consumer.sh --topic word-count-topic \
  --from-beginning \
  --bootstrap-server localhost:9092 \
  --property print.key=true \
  --property key.separator=" : " \
  --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" \
  --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```
