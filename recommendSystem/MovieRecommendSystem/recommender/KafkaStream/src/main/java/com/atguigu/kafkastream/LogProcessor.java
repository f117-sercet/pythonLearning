package com.atguigu.kafkastream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/7/24 18:18
 */
public class LogProcessor implements Processor<byte[], byte[]> {

    private ProcessorContext context;

    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
    }

    @Override
    public void process(byte[] bytes, byte[] bytes2) {

    }

    @Override
    public void punctuate(long l) {

    }

    @Override
    public void close() {

    }
}
