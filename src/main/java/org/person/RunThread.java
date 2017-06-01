package org.person;

/**
 * Created by youtNa on 2017/5/22.
 */
public class RunThread {
    public RunThread(){
        ConsumerKafka kafka = new ConsumerKafka();
        kafka.start();
    }
}
