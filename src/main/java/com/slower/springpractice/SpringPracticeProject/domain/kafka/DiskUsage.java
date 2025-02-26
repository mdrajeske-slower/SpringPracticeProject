package com.slower.springpractice.SpringPracticeProject.domain.kafka;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DiskUsage {
    private String topicName;
    private long diskUsage;
}
