package com.ajax_systems.akka.service;

import akka.actor.ActorSystem;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.ajax_systems.akka.service.dto.SomeTxtLevelDTO;
import com.ajax_systems.akka.service.dto.SomeTxtDTO;

import java.util.Collection;


public interface SomeTxtService {

    SomeTxtService init(ActorSystem system);

    /**
     *
     * @param data
     * @return
     */
    void upload(Source<ByteString, Object> data);

    /**
     *
     * @return
     */

    SomeTxtDTO findSomeTxtOne(String id);

    Collection<SomeTxtDTO> findSomeTxt();

    SomeTxtLevelDTO findSomeTxtLevelOne(String id);

    Collection<SomeTxtLevelDTO> findSomeTxtLevel();

    Collection<SomeTxtLevelDTO> findSomeTxtLevelByLevel(String level);
}
