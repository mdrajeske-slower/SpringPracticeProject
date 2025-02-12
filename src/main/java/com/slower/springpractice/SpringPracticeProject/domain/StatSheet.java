package com.slower.springpractice.SpringPracticeProject.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatSheet {
    private String familyName;
    private String characterName;
    private int ap;
    private int dp;

    //TODO: implement this last
    private List<MiscStat> miscStats;
}
