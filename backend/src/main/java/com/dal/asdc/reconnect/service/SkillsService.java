package com.dal.asdc.reconnect.service;


import com.dal.asdc.reconnect.DTO.SkillsResponseBody;
import com.dal.asdc.reconnect.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsService {
    @Autowired
    SkillsRepository skillsRepository;


    public SkillsResponseBody getSkills() {

        SkillsResponseBody responseBody = new SkillsResponseBody();

        responseBody.setSkills(skillsRepository.findAll());

        return responseBody;

    }
}
