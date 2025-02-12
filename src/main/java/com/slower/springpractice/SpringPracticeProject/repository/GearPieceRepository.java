package com.slower.springpractice.SpringPracticeProject.repository;

import com.slower.springpractice.SpringPracticeProject.domain.GearPieceDAO;
import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GearPieceRepository extends MongoRepository<GearPieceDAO, String> {
    GearPieceDAO findByName(String name);
}
