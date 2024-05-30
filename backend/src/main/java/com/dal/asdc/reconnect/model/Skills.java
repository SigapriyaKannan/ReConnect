package com.dal.asdc.reconnect.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="Skills")
@Data
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SkillID")
    private int skillId;

    @Column(name = "SkillName", nullable = false, length = 100)
    private String skillName;

    @Column(name = "SkillDomain", length = 100)
    private String skillDomain;
}
