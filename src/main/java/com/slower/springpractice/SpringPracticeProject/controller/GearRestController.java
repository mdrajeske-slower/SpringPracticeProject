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

    @PostMapping("/new_gear_sheet")
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

    @DeleteMapping("/delete_gear_sheet/{id}")
    public ResponseEntity<Void> deleteGearSheet(@PathVariable("id") String id){
        gearService.deleteGearSheet(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: PUT mapping for updating a gear sheet

    @GetMapping("/calculate_stats")
    public ResponseEntity<List<StatSheet>> calculateGearSheetStats(
            @RequestParam(name = "family_name", required = true) String familyName,
            @RequestParam(name = "character_name") String characterName){
        return ResponseEntity.ok()
                .body(gearService.getStatSheets(familyName, characterName));
    }
}
