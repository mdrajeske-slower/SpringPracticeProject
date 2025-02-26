package com.slower.springpractice.SpringPracticeProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slower.springpractice.SpringPracticeProject.controller.GearRestController;
import com.slower.springpractice.SpringPracticeProject.domain.GearSheetDAO;
import com.slower.springpractice.SpringPracticeProject.domain.GearSlot;
import com.slower.springpractice.SpringPracticeProject.repository.GearPieceRepository;
import com.slower.springpractice.SpringPracticeProject.repository.GearSheetRepository;
import com.slower.springpractice.SpringPracticeProject.services.GearService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GearRestController.class)
public class ControllerWebMVCTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GearService gearService;

    @MockBean
    GearPieceRepository gearPieceRepository;

    @MockBean
    GearSheetRepository gearSheetRepository;

    @Test
    public void testGetEndpoints() throws Exception {
        mockMvc.perform(get("/gear_sheet")
                        .param("family_name", "ThisIsAFamilyName")
                        .param("character_name", "ThisIsACharacterName"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gear_piece")
                        .param("name", "Kharazad Earring"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/calculate_stats")
                        .param("family_name", "ThisIsAFamilyName")
                        .param("character_name", "ThisIsACharacterName"))
                .andExpect(status().isOk());
    }


    @Test
    public void testPostEndpoints() throws Exception {
        GearSheetDAO request = new GearSheetDAO();
        request.setCharacterName("ArthasTest");
        request.setFamilyName("Menethil");
        request.setChestPieceSlot(new GearSlot("Slumbering Origin Armor", 3));
        request.setGlovesSlot(new GearSlot("Dahn's Gloves", 4));
        request.setBootsSlot(new GearSlot("Ator's Shoes", 3));
        request.setHelmetSlot(new GearSlot("Labreska Helmet", 4));
        request.setNecklace(new GearSlot("Kharazad Necklace", 7));
        request.setBelt(new GearSlot("Kharazad Belt", 9));
        request.setEarringSlot1(new GearSlot("Kharazad Earring", 5));
        request.setEarringSlot2(new GearSlot("Kharazad Earring", 5));
        request.setRingSlot1(new GearSlot("Kharazad Ring", 9));
        request.setRingSlot2(new GearSlot("Kharazad Ring", 9));
        request.setCrystals(List.of("Crystal of Fortitude", "Crystal of Precision", "Crystal of Endurance"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/gear_sheet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)) // Pass the JSON body
                .andExpect(status().isOk());
    }

    @Test
    public void testPutEndpoints() throws Exception {
        GearSheetDAO request = new GearSheetDAO();
        request.setCharacterName("ArthasTest");
        request.setFamilyName("Menethil");
        request.setChestPieceSlot(new GearSlot("Slumbering Origin Armor", 4));
        request.setGlovesSlot(new GearSlot("Dahn's Gloves", 4));
        request.setBootsSlot(new GearSlot("Ator's Shoes", 4));
        request.setHelmetSlot(new GearSlot("Labreska Helmet", 4));
        request.setNecklace(new GearSlot("Kharazad Necklace", 7));
        request.setBelt(new GearSlot("Kharazad Belt", 9));
        request.setEarringSlot1(new GearSlot("Kharazad Earring", 5));
        request.setEarringSlot2(new GearSlot("Kharazad Earring", 5));
        request.setRingSlot1(new GearSlot("Kharazad Ring", 9));
        request.setRingSlot2(new GearSlot("Kharazad Ring", 9));
        request.setCrystals(List.of("Crystal of Fortitude", "Crystal of Precision", "Crystal of Endurance"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/gear_sheet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)) // Pass the JSON body
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEndpoints() throws Exception {
        mockMvc.perform(delete("/gear_sheet")
                        .param("family_name", "ArthasTest")
                        .param("character_name", "Menethil"))
                .andExpect(status().isNoContent());
    }

}
