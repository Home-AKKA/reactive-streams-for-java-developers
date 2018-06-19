package com.ajax_systems.akka.service.mapper;


import com.ajax_systems.akka.service.dto.SomeTxtLevelDTO;
import com.ajax_systems.akka.model.SomeTxtLevel;

/**
 * Mapper for the entity SomeTxtLevel and its DTO SomeTxtLevelDTO.
 */
public interface SomeTxtLevelMapper extends EntityMapper<SomeTxtLevelDTO, SomeTxtLevel> {

    SomeTxtLevelDTO toDto(SomeTxtLevel entity);

    SomeTxtLevel toEntity(SomeTxtLevelDTO someTxtLevelDTO);
}
