package com.slower.springpractice.SpringPracticeProject.controller;

import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import com.slower.springpractice.SpringPracticeProject.domain.kafka.ConsumerGroupInfo;
import com.slower.springpractice.SpringPracticeProject.domain.kafka.DiskUsage;
import com.slower.springpractice.SpringPracticeProject.domain.kafka.KafkaStatus;
import com.slower.springpractice.SpringPracticeProject.domain.kafka.TopicConfig;
import com.slower.springpractice.SpringPracticeProject.services.GearService;
import com.slower.springpractice.SpringPracticeProject.services.KafkaService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KafkaRestController {

    private final KafkaService kafkaService;

    @PostMapping("/create_topic")
    public void createTopic(@RequestBody TopicConfig topicConfig){
        kafkaService.createTopic(topicConfig);
    }

    @DeleteMapping("/delete_topic")
    public ResponseEntity<Void> deleteTopic(@RequestParam("topic_name") String topicName){
        kafkaService.deleteTopic(topicName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/produce_message")
    public void sendMessage(@RequestParam("message") String message,
                            @RequestParam("topic_name") String topicName){
        kafkaService.produceMessage(message, topicName);
    }

    @GetMapping("/status")
    public ResponseEntity<KafkaStatus> getStatus(){
        return ResponseEntity.ok().body(kafkaService.getStatus());
    }

    @PostMapping("/create_consumer")
    public void createConsumer(@RequestParam("group_id") String groupId,
                               @RequestParam("topic_name") String topicName){
        kafkaService.createConsumer(topicName, groupId);
    }

    @GetMapping("/consumer")
    public ResponseEntity<ConsumerGroupInfo> getConsumer(@RequestParam("group_id") String groupId){
        return ResponseEntity.ok().body(kafkaService.getConsumerGroupInfo(groupId));
    }

    @DeleteMapping("/consumer")
    public ResponseEntity<Void> deleteConsumerGroup(@RequestParam("group_id") String groupId){
        kafkaService.deleteConsumer(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disk_usage")
    public ResponseEntity<List<DiskUsage>> getDiskUsage(){
        return ResponseEntity.ok().body(kafkaService.getDiskUsageByTopic());
    }
}
