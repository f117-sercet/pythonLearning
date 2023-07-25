package com.atguigu.kafkastream;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.sql.SQLOutput;

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
    public void process(byte[] dummy, byte[] line) {

        // 把收集到的日志信息用string表述
        String input = new String(line);
        if (input.contains("MOVIE_RATTING_PREFIX:")){
            System.out.println("movie ratting data coming!>>>>>>>>>>"+input);
            input = input.split("MOVIE_RATING_PREFIX:")[1].trim();
            context.forward( "logProcessor".getBytes(), input.getBytes() );
        }


    }

    @Override
    public void punctuate(long l) {

    }

    @Override
    public void close() {

    }
}
