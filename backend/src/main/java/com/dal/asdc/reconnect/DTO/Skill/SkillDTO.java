package com.dal.asdc.reconnect.DTO.Skill;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillDTO
{
    private int skillId;
    private String skillName;
    private String domainName;
}
