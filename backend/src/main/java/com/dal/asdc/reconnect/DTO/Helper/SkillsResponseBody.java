package com.dal.asdc.reconnect.DTO.Helper;

import com.dal.asdc.reconnect.model.Skills;
import lombok.Data;

import java.util.List;


@Data
public class SkillsResponseBody {

    List<SkillsDTO> skills;
}
