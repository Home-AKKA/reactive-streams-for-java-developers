package com.ajax_systems.akka.service.mapper.impl;


import com.ajax_systems.akka.service.mapper.SomeTxtMapper;
import com.ajax_systems.akka.model.SomeTxt;
import com.ajax_systems.akka.service.dto.SomeTxtDTO;

public class SomeTxtMapperImpl implements SomeTxtMapper {

    public SomeTxtDTO toDto(SomeTxt entity) {
        if (entity == null) {
            return null;
        } else {
            SomeTxtDTO someTxtDTO = new SomeTxtDTO();
            someTxtDTO.setLevel(entity.getLevel());
            someTxtDTO.setTotal(entity.getTotal());
            return someTxtDTO;
        }
    }

    public SomeTxt toEntity(SomeTxtDTO someTxtDTO) {
        if (someTxtDTO == null) {
            return null;
        } else {
            SomeTxt someTxt = new SomeTxt();
            someTxt.setLevel(someTxtDTO.getLevel());
            someTxt.setTotal(someTxtDTO.getTotal());
            return someTxt;
        }
    }
}
