package com.ajax_systems.akka.service.mapper.impl;


import com.ajax_systems.akka.service.dto.SomeTxtLevelDTO;
import com.ajax_systems.akka.model.SomeTxtLevel;
import com.ajax_systems.akka.service.mapper.SomeTxtLevelMapper;

public class SomeTxtLevelMapperImpl implements SomeTxtLevelMapper {

    public SomeTxtLevelDTO toDto(SomeTxtLevel entity) {
        if (entity == null) {
            return null;
        } else {
            SomeTxtLevelDTO someTxtLevelDTO = new SomeTxtLevelDTO();
            someTxtLevelDTO.setId(entity.getId());
            someTxtLevelDTO.setLevel(entity.getLevel());
            someTxtLevelDTO.setDescription(entity.getDescription());
            someTxtLevelDTO.setSize(entity.getSize());
            return someTxtLevelDTO;
        }
    }

    public SomeTxtLevel toEntity(SomeTxtLevelDTO someTxtLevelDTO) {
        if (someTxtLevelDTO == null) {
            return null;
        } else {
            SomeTxtLevel someTxtLevel = new SomeTxtLevel();
            someTxtLevel.setId(someTxtLevelDTO.getId());
            someTxtLevel.setLevel(someTxtLevelDTO.getLevel());
            someTxtLevel.setDescription(someTxtLevelDTO.getDescription());
            someTxtLevel.setSize(someTxtLevelDTO.getSize());
            return someTxtLevel;
        }
    }
}
