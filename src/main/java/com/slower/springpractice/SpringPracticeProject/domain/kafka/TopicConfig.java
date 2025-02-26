package com.slower.springpractice.SpringPracticeProject.domain.kafka;

import lombok.*;

@Builder
@Getter
@Setter
public class TopicConfig {
    private String topicName;
    private int partitions;
    private short replicationFactor;
}
