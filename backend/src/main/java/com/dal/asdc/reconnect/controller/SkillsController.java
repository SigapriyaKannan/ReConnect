package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.DTO.Helper.SkillsDTO;
import com.dal.asdc.reconnect.DTO.Response;
import com.dal.asdc.reconnect.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "http://localhost:4200")
public class SkillsController {
    @Autowired
    SkillsService skillsService;

    @GetMapping("/getAllSkills")
    public ResponseEntity<?> getAllSkills() {
        List<SkillsDTO> listOfSkills = skillsService.getSkills();
        Response<List<SkillsDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all skills", listOfSkills);
        return ResponseEntity.ok(response);
    }
}
