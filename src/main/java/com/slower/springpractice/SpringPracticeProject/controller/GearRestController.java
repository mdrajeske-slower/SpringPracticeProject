package com.slower.springpractice.SpringPracticeProject.controller;
import com.slower.springpractice.SpringPracticeProject.domain.GearPieceDAO;
import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import com.slower.springpractice.SpringPracticeProject.domain.StatSheet;
import com.slower.springpractice.SpringPracticeProject.services.GearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GearRestController {
    private final GearService gearService;

    @PostMapping("/gear_sheet")
    public void createNewGearSheet(@RequestBody GearSheetDAO gearSheet){
        gearService.addNewGearSheet(gearSheet);
    }

    @GetMapping("/gear_sheet")
    public ResponseEntity<List<GearSheetDAO>> getGearSheet(
            @RequestParam(name = "family_name", required = true) String familyName,
            @RequestParam(name = "character_name") String characterName){
        return ResponseEntity.ok()
                .body(gearService.getGearSheets(familyName, characterName));
    }


    @GetMapping("/gear_piece")
    public ResponseEntity<GearPieceDAO> getGearPiece(
            @RequestParam(name = "name", required = true) String name){
        return ResponseEntity.ok()
                .body(gearService.getGearPiece(name));
    }

    @DeleteMapping("/gear_sheet")
    public ResponseEntity<Void> deleteGearSheet(@RequestParam("family_name") String familyName, @RequestParam("character_name") String characterName){
        gearService.deleteGearSheet(familyName, characterName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/gear_sheet")
    public void updateExistingGearSheet(
            @RequestBody GearSheetDAO gearSheet){
        gearService.updateGearSheet(gearSheet);
    }

    @GetMapping("/calculate_stats")
    public ResponseEntity<List<StatSheet>> calculateGearSheetStats(
            @RequestParam(name = "family_name", required = true) String familyName,
            @RequestParam(name = "character_name") String characterName){
        return ResponseEntity.ok()
                .body(gearService.getStatSheets(familyName, characterName));
    }
}
