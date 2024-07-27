package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.dto.Skill.SkillDomainDTO;
import com.dal.asdc.reconnect.model.SkillDomain;
import com.dal.asdc.reconnect.repository.SkillDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkillDomainService {
    @Autowired
    SkillDomainRepository skillDomainRepository;

    public List<SkillDomainDTO> getAllSkillDomains() {
        log.debug("Fetching all skill domains");
        return skillDomainRepository.findAll().stream().map(domain -> new SkillDomainDTO(domain.getDomainId(), domain.getDomainName())).collect(Collectors.toList());
    }

    public void addSkillDomain(SkillDomainDTO skillDomainDTO) {
        log.info("Adding new skill domain: {}", skillDomainDTO);
        SkillDomain skillDomain = new SkillDomain();
        skillDomain.setDomainName(skillDomainDTO.getDomainName());
        skillDomainRepository.save(skillDomain);
    }

    public void editSkillDomain(SkillDomainDTO skillDomainDTO) {
        log.info("Adding new skill domain: {}", skillDomainDTO);
        SkillDomain skillDomain = skillDomainRepository.findById(skillDomainDTO.getDomainId()).orElseThrow(() -> new RuntimeException("Skill domain not found"));
        skillDomain.setDomainName(skillDomainDTO.getDomainName());
        skillDomainRepository.save(skillDomain);
    }

    public void deleteSkillDomain(Integer id) {
        skillDomainRepository.deleteById(id);
    }
}
