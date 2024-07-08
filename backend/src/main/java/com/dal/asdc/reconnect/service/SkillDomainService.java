package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.DTO.Skill.SkillDomainDTO;
import com.dal.asdc.reconnect.model.SkillDomain;
import com.dal.asdc.reconnect.repository.SkillDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillDomainService {
    @Autowired
    SkillDomainRepository skillDomainRepository;

    public List<SkillDomainDTO> getAllSkillDomains() {
        return skillDomainRepository.findAll().stream().map(domain -> new SkillDomainDTO(domain.getDomainId(), domain.getDomainName())).collect(Collectors.toList());
    }

    public void addSkillDomain(SkillDomainDTO skillDomainDTO) {
        SkillDomain skillDomain = new SkillDomain();
        skillDomain.setDomainName(skillDomainDTO.getDomainName());
        skillDomainRepository.save(skillDomain);
    }

    public void editSkillDomain(SkillDomainDTO skillDomainDTO) {
        SkillDomain skillDomain = skillDomainRepository.findById(skillDomainDTO.getDomainId()).orElseThrow(() -> new RuntimeException("Skill domain not found"));
        skillDomain.setDomainName(skillDomainDTO.getDomainName());
        skillDomainRepository.save(skillDomain);
    }

    public void deleteSkillDomain(Integer id) {
        skillDomainRepository.deleteById(id);
    }
}
