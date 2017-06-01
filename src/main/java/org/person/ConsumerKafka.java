package org.person;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static org.person.WebSocket.wbSockets;

/**
 * Created by youtNa on 2017/5/22.
 */
public class ConsumerKafka extends Thread {

    private KafkaConsumer<String,String> consumer;
    private String topic = "kafkaTopic";

    public ConsumerKafka(){

    }

    @Override
    public void run(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "ytna");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "15000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String,String>(props);
        consumer.subscribe(Arrays.asList(this.topic));
        while (true){
            try {
                ConsumerRecords<String, String> records = consumer.poll(100);
                //Consumer message
                for (ConsumerRecord<String, String> record : records) {
                    //Send message to every client
                    for (WebSocket webSocket :wbSockets){
                        webSocket.sendMessage(record.value());
                    }
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }

    public void close() {
        try {
            consumer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        ConsumerKafka consumerKafka = new ConsumerKafka();
        consumerKafka.start();
    }
}
