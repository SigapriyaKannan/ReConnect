package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.dto.Skill.SkillsDto;
import com.dal.asdc.reconnect.model.SkillDomain;
import com.dal.asdc.reconnect.model.Skills;
import com.dal.asdc.reconnect.repository.SkillDomainRepository;
import com.dal.asdc.reconnect.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SkillsService {
    @Autowired
    SkillsRepository skillsRepository;

    @Autowired
    SkillDomainRepository skillDomainRepository;

    /**
     * Retrieves the list of all skills.
     *
     * @return a list of SkillsDTO containing the list of all skills.
     */
    public List<SkillsDto> getSkills() {
        log.debug("Fetching all skills");
        List<SkillsDto> listOfSkills = new ArrayList<>();
        List<Skills> listOfSkillsFromDatabase = skillsRepository.findAll();

        for (Skills skill : listOfSkillsFromDatabase) {
            SkillsDto skillsDTO = new SkillsDto(
                    skill.getSkillId(),
                    skill.getSkillName(),
                    skill.getSkillDomain().getDomainId(),
                    skill.getSkillDomain().getDomainName()
            );
            listOfSkills.add(skillsDTO);
        }

        return listOfSkills;
    }

    public void addSkill(SkillsDto skillDTO) {
        log.info("Adding new skill: {}", skillDTO);
        Skills skill = new Skills();
        skill.setSkillName(skillDTO.getSkillName());
        SkillDomain domain = skillDomainRepository.findById(skillDTO.getDomainId())
                .orElseThrow(() -> new RuntimeException("Skill Domain not found"));
        skill.setSkillDomain(domain);
        skillsRepository.save(skill);
    }

    public void editSkill(SkillsDto skillDTO) {
        log.info("Editing skill with ID: {}", skillDTO.getSkillId());
        Skills skill = skillsRepository.findById(skillDTO.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skill.setSkillName(skillDTO.getSkillName());
        SkillDomain domain = skillDomainRepository.findById(skillDTO.getDomainId())
                .orElseThrow(() -> new RuntimeException("Skill Domain not found"));
        skill.setSkillDomain(domain);
        skillsRepository.save(skill);
    }

    public void deleteSkill(Integer id) {
        skillsRepository.deleteById(id);
    }
}
