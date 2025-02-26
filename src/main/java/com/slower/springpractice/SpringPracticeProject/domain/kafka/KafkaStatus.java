package com.slower.springpractice.SpringPracticeProject.domain.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class KafkaStatus {
    private int availBrokerCount;
    private int underReplicatedPartitions;
    private List<String> topicList;
}
