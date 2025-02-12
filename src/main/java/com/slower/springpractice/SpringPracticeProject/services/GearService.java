package com.slower.springpractice.SpringPracticeProject.services;

import com.slower.springpractice.SpringPracticeProject.domain.GearPieceDAO;
import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import com.slower.springpractice.SpringPracticeProject.domain.GearSlot;
import com.slower.springpractice.SpringPracticeProject.domain.StatSheet;
import com.slower.springpractice.SpringPracticeProject.repository.GearPieceRepository;
import com.slower.springpractice.SpringPracticeProject.repository.GearSheetRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GearService {
    private final GearSheetRepository gearSheetRepo;
    private final GearPieceRepository gearPieceRepo;

    public GearSheetDAO addNewGearSheet(GearSheetDAO gearSheet){
        return gearSheetRepo.save(gearSheet);
    }

    public List<GearSheetDAO> getGearSheets(String familyName, String characterName){
        if(!characterName.isEmpty()) {
            return gearSheetRepo.findByFamilyNameAndCharacterName(familyName, characterName);
        }
        return gearSheetRepo.findByFamilyName(familyName);
    }

    public GearPieceDAO getGearPiece(String name){
        return gearPieceRepo.findByName(name);
    }

    public List<StatSheet> getStatSheets(String familyName, String characterName) {
        return gearSheetRepo.findByFamilyNameAndCharacterName(familyName, characterName)
                .stream()
                .map(gearSheet -> {
                    Map<String, List<GearPieceDAO>> gearStats = Stream.of(
                                    gearSheet.getChestPieceSlot(),
                                    gearSheet.getHelmetSlot(),
                                    gearSheet.getGlovesSlot(),
                                    gearSheet.getBootsSlot(),
                                    gearSheet.getBelt(),
                                    gearSheet.getNecklace(),
                                    gearSheet.getEarringSlot1(),
                                    gearSheet.getEarringSlot2(),
                                    gearSheet.getRingSlot1(),
                                    gearSheet.getRingSlot2()
                            )
                            .collect(Collectors.toMap(
                                    GearSlot::getName,
                                    slot -> new ArrayList<>(List.of(gearPieceRepo.findByName(slot.getName()))), // Store as a List
                                    (existing, replacement) -> {
                                        existing.addAll(replacement); // Merge duplicates into the list
                                        return existing;
                                    }
                            ));


                    int chestPieceDp = calculateDp(gearSheet.getChestPieceSlot().getLevel(), gearStats.get(gearSheet.getChestPieceSlot().getName()));
                    int helmetDp = calculateDp(gearSheet.getHelmetSlot().getLevel(), gearStats.get(gearSheet.getHelmetSlot().getName()));
                    int glovesDp = calculateDp(gearSheet.getGlovesSlot().getLevel(), gearStats.get(gearSheet.getGlovesSlot().getName()));
                    int bootsDp = calculateDp(gearSheet.getBootsSlot().getLevel(), gearStats.get(gearSheet.getBootsSlot().getName()));

                    int necklaceAp = calculateAp(gearSheet.getNecklace().getLevel(), gearStats.get(gearSheet.getNecklace().getName()));
                    int beltAp = calculateAp(gearSheet.getBelt().getLevel(), gearStats.get(gearSheet.getBelt().getName()));
                    int earring1Ap = calculateAp(gearSheet.getEarringSlot1().getLevel(), gearStats.get(gearSheet.getEarringSlot1().getName()));
                    int earring2Ap = calculateAp(gearSheet.getEarringSlot2().getLevel(), gearStats.get(gearSheet.getEarringSlot2().getName()));
                    int ring1Ap = calculateAp(gearSheet.getRingSlot1().getLevel(), gearStats.get(gearSheet.getRingSlot1().getName()));
                    int ring2Ap = calculateAp(gearSheet.getRingSlot2().getLevel(), gearStats.get(gearSheet.getRingSlot2().getName()));


                    return StatSheet.builder()
                            .ap(ring1Ap + ring2Ap + necklaceAp + beltAp + earring1Ap + earring2Ap)
                            .dp(chestPieceDp + helmetDp + glovesDp + bootsDp)
                            .familyName(familyName)
                            .characterName(characterName)
                            .miscStats(new ArrayList<>())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private int calculateDp(int level, List<GearPieceDAO> stats) {
        return stats.stream().mapToInt(s -> level * s.getDpScaling() + s.getDp()).sum();
    }

    private int calculateAp(int level, List<GearPieceDAO> stats) {
        return stats.stream().mapToInt(s -> level * s.getApScaling() + s.getAp()).sum();
    }


    public void deleteGearSheet(String id){
        gearSheetRepo.deleteById(id);
    }
}
