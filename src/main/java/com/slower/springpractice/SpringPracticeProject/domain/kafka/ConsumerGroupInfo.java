package com.slower.springpractice.SpringPracticeProject.domain.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.MemberDescription;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
public class ConsumerGroupInfo {
    private String groupId;
    private List<SimpleMemberDescription> members;
}
