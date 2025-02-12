package com.slower.springpractice.SpringPracticeProject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("GearPiece")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GearPieceDAO {
    @Id
    @Field("_id")
    private String id;

    @Field("Name")
    private String name;

    @Field("Slot")
    private String slot;

    @Field("AP")
    private int ap;

    @Field("DP")
    private int dp;

    @Field("APScaling")
    private int apScaling;

    @Field("DPScaling")
    private int dpScaling;


}
