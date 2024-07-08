package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.DTO.Skill.SkillsDto;
import com.dal.asdc.reconnect.model.SkillDomain;
import com.dal.asdc.reconnect.model.Skills;
import com.dal.asdc.reconnect.repository.SkillDomainRepository;
import com.dal.asdc.reconnect.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillsService {
    @Autowired
    SkillsRepository skillsRepository;

    @Autowired
    SkillDomainRepository skillDomainRepository;

    /**
     * Retrieves the list of all skills.
     * @return a list of SkillsDTO containing the list of all skills.
     */
    public List<SkillsDto> getSkills() {
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
        Skills skill = new Skills();
        skill.setSkillName(skillDTO.getSkillName());
        SkillDomain domain = skillDomainRepository.findById(skillDTO.getDomainId())
                .orElseThrow(() -> new RuntimeException("Skill Domain not found"));
        skill.setSkillDomain(domain);
        skillsRepository.save(skill);
    }

    public void editSkill(SkillsDto skillDTO) {
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
