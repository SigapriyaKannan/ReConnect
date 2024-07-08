package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.DTO.Helper.SkillsDTO;
import com.dal.asdc.reconnect.DTO.Helper.SkillsResponseBody;
import com.dal.asdc.reconnect.model.Skills;
import com.dal.asdc.reconnect.model.UserSkills;
import com.dal.asdc.reconnect.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillsService {
    @Autowired
    SkillsRepository skillsRepository;

    /**
     * Retrieves the list of all skills.
     * @return a SkillsResponseBody object containing the list of all skills.
     */
    public List<SkillsDTO> getSkills() {
        List<SkillsDTO> listOfSkills = new ArrayList<>();
        List<Skills> listOfSkillsFromDatabase = skillsRepository.findAll();

        for(Skills skill: listOfSkillsFromDatabase) {
            SkillsDTO skillsDTO = new SkillsDTO(skill.getSkillId(), skill.getSkillName(), skill.getSkillDomain());
            listOfSkills.add(skillsDTO);
        }

        return listOfSkills;
    }
}
