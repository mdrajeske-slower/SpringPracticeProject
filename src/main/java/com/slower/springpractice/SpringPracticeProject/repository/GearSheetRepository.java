package com.slower.springpractice.SpringPracticeProject.repository;

import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GearSheetRepository extends MongoRepository<GearSheetDAO, String> {
    List<GearSheetDAO> findByFamilyName(String familyName);

    List<GearSheetDAO> findByFamilyNameAndCharacterName(String familyName, String characterName);

    void deleteById(String Id);
}
