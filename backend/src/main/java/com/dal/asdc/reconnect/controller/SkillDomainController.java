package com.dal.asdc.reconnect.controller;

import com.dal.asdc.reconnect.dto.Response;
import com.dal.asdc.reconnect.dto.Skill.SkillDomainDTO;
import com.dal.asdc.reconnect.service.SkillDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skillDomains")
@CrossOrigin(origins = "http://localhost:4200")
public class SkillDomainController {
    @Autowired
    SkillDomainService skillDomainService;

    @GetMapping("/getAllSkillDomains")
    public ResponseEntity<?> getAllSkillDomains() {
        List<SkillDomainDTO> listOfSkillDomains = skillDomainService.getAllSkillDomains();
        Response<List<SkillDomainDTO>> response = new Response<>(HttpStatus.OK.value(), "Fetched all skill domains", listOfSkillDomains);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addSkillDomain")
    public ResponseEntity<?> addSkillDomain(@RequestBody SkillDomainDTO skillDomainDTO) {
        skillDomainService.addSkillDomain(skillDomainDTO);
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Skill domain added successfully", null));
    }

    @PutMapping("/editSkillDomain")
    public ResponseEntity<?> editSkillDomain(@RequestBody SkillDomainDTO skillDomainDTO) {
        skillDomainService.editSkillDomain(skillDomainDTO);
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Skill domain updated successfully", null));
    }

    @DeleteMapping("/deleteSkillDomain/{id}")
    public ResponseEntity<?> deleteSkillDomain(@PathVariable Integer id) {
        skillDomainService.deleteSkillDomain(id);
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Skill domain deleted successfully", null));
    }
}
