package com.recommender;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;

import java.util.Properties;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2024/7/23 15:06
 */
public class kafkaRecommender {
    public static void main(String[] args) {
        String brokers = "influxdb:9092";
        String zookeepers = "influx:2181";

        // 定义输入和输出的topic
        String from ="log";
        String to = "recommender";

        // 定义kafkaStreaming的 配置
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG,"logFilter");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        settings.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zookeepers);
        StreamsConfig config = new StreamsConfig(settings);

        // 构件拓扑器
        TopologyBuilder builder = new TopologyBuilder();

        // 定义流处理的拓扑结构
        builder.addSource("SOURCE", from)
                .addProcessor("PROCESS", () -> new LogProcessor(), "SOURCE")
                .addSink("SINK", to, "PROCESS");
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

    }
}
