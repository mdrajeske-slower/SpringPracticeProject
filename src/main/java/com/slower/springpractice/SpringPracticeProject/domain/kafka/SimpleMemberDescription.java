package com.slower.springpractice.SpringPracticeProject.domain.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class SimpleMemberDescription {
    String topicName;
    String memberName;
}
