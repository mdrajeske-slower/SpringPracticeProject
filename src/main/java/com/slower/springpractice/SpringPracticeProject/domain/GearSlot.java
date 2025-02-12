package com.slower.springpractice.SpringPracticeProject.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// This is a class to represent the fields within a gear sheet document.
// These are the fields that represent pieces of gear.

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GearSlot {
    @Field("Name")
    private String name;

    @Field("Level")
    private int level;
}
