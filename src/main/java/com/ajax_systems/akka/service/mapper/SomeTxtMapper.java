package com.ajax_systems.akka.service.mapper;


import com.ajax_systems.akka.model.SomeTxt;
import com.ajax_systems.akka.service.dto.SomeTxtDTO;

/**
 * Mapper for the entity SomeTxt and its DTO SomeTxtDTO.
 */
public interface SomeTxtMapper extends EntityMapper<SomeTxtDTO, SomeTxt> {

    SomeTxtDTO toDto(SomeTxt entity);

    SomeTxt toEntity(SomeTxtDTO someTxtDTO);
}
