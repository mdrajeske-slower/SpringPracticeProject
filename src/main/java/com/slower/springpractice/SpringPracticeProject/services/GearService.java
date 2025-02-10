package com.slower.springpractice.SpringPracticeProject.services;

import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import com.slower.springpractice.SpringPracticeProject.repository.GearSheetRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GearService {
    private final GearSheetRepository gearSheetRepo;

    public GearSheetDAO addNewGearSheet(GearSheetDAO gearSheet){
        return gearSheetRepo.save(gearSheet);
    }

    public List<GearSheetDAO> getGearSheets(String familyName, String characterName){
        if(!characterName.isEmpty()) {
            return gearSheetRepo.findByFamilyNameAndCharacterName(familyName, characterName);
        }
        return gearSheetRepo.findByFamilyName(familyName);
    }

    public void deleteGearSheet(String id){
        gearSheetRepo.deleteById(id);
    }
}
