package com.slower.springpractice.SpringPracticeProject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "GearSheet")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GearSheetDAO {
    @Id
    @Field("_id")
    private String id;

    @Field("CharacterName")
    private final String characterName;

    @Field("FamilyName")
    private final String familyName;

    @Field("ChestPiece")
    private GearSlot chestPieceSlot;

    @Field("Gloves")
    private GearSlot glovesSlot;

    @Field("Boots")
    private GearSlot bootsSlot;

    @Field("Helmet")
    private GearSlot helmetSlot;

    @Field("Earring1")
    private GearSlot earringSlot1;

    @Field("Earring2")
    private GearSlot earringSlot2;

    @Field("Ring1")
    private GearSlot ringSlot1;

    @Field("Ring2")
    private GearSlot ringSlot2;

    @Field("Crystals")
    private List<String> crystals;
}
