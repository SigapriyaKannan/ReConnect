package com.dal.asdc.reconnect.DTO;

import com.dal.asdc.reconnect.model.Skills;
import lombok.Data;

import java.util.List;


@Data
public class SkillsResponseBody {

    List<Skills> skills;
}
