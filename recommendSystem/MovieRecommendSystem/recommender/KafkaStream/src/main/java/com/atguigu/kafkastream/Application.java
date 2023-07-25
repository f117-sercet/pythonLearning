package com.atguigu.kafkastream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;

import java.util.Properties;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/7/24 18:15
 */
public class Application {

    public static void main(String[] args) {

        String brokers ="localhost:9092";
        String zookeepers ="localhost:2181";

        // 输入输出的topic
        String from = "log";
        String to = "recommender";

        //定义卡法卡 streaming 的配置

        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "logFilter");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        settings.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zookeepers);

        TopologyBuilder builder = new TopologyBuilder();

        // 创建 kafka stream 配置对象
        StreamsConfig config = new StreamsConfig(settings);

        // 定义流处理的拓扑结构
        builder.addSource("SOURCE", from)
                .addProcessor("PROCESSOR", ()->new LogProcessor(), "SOURCE")
                .addSink("SINK", to, "PROCESSOR");

        KafkaStreams streams = new KafkaStreams( builder, config );

        streams.start();

        System.out.println("Kafka stream started!>>>>>>>>>>>");


    }

}
