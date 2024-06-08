package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.DTO.Helper.SkillsDTO;
import com.dal.asdc.reconnect.DTO.Helper.SkillsResponseBody;
import com.dal.asdc.reconnect.model.Skills;
import com.dal.asdc.reconnect.model.UserSkills;
import com.dal.asdc.reconnect.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public SkillsResponseBody getSkills() {

        SkillsResponseBody skillsResponseBody = new SkillsResponseBody();
        List<Skills> userSkills = skillsRepository.findAll();

        List<SkillsDTO> skillsDTOs = userSkills.stream()
                .map(skill -> new SkillsDTO(skill.getSkillId(), skill.getSkillName()))
                .collect(Collectors.toList());

        skillsResponseBody.setSkills(skillsDTOs);
        return skillsResponseBody;

    }
}
