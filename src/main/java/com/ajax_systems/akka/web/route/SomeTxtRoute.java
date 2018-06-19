package com.ajax_systems.akka.web.route;

import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import com.ajax_systems.akka.service.SomeTxtService;
import com.ajax_systems.akka.service.dto.SomeTxtLevelDTO;
import com.ajax_systems.akka.service.impl.SomeTxtServiceImpl;
import com.ajax_systems.akka.service.dto.SomeTxtDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Function;


public class SomeTxtRoute extends HttpApp {

    private static final Logger log = LoggerFactory.getLogger(SomeTxtRoute.class);

    private SomeTxtService someTxtService;

    @Inject
    public SomeTxtRoute(SomeTxtServiceImpl someTxtService) {
        this.someTxtService = someTxtService
                .init(ActorSystem.create("system-txt"));
    }

    public Route findSomeTxtAll() {
        Collection<SomeTxtDTO> someTxts = someTxtService.findSomeTxt();
        return complete(StatusCodes.OK, someTxts, Jackson.marshaller());
    }

    public Route findSomeTxtLevelAll() {
        Collection<SomeTxtLevelDTO> someTxtLevels = someTxtService.findSomeTxtLevel();
        return complete(StatusCodes.OK, someTxtLevels, Jackson.marshaller());
    }

    public Function<String, Route> findSomeTxtById = id -> {
        SomeTxtDTO someTxt = someTxtService.findSomeTxtOne(id);
        return (someTxt == null) ? reject() : complete(StatusCodes.OK, someTxt, Jackson.marshaller());
    };

    public Function<String, Route> findSomeTxtLevelByLevel = level -> {
        Collection<SomeTxtLevelDTO> someTxtLevels = someTxtService.findSomeTxtLevelByLevel(level);
        return (someTxtLevels == null) ? reject() : complete(StatusCodes.OK, someTxtLevels, Jackson.marshaller());
    };

    @Override
    public Route routes() {
        return route(
                path("", () ->
                        getFromResource("web/index.html")
                ),

                pathPrefix("txt-logs", () ->
                        pathEndOrSingleSlash(() -> route(
//                              log.debug("post http://localhost:8080/txt-logs");
                                post(() ->
                                        entity(Unmarshaller.entityToMultipartFormData(), data -> {
                                            someTxtService.upload(data.toEntity().getDataBytes());
                                            return findSomeTxtAll();
                                        })),

//                               log.debug("get http://localhost:8080/txt-logs");
                                get(() -> findSomeTxtAll())
                        ))),

//              log.debug("get http://localhost:8080/txt-logs/{}", id);
                pathPrefix("txt-logs", () ->
                        path(id -> route(
                                get(() -> findSomeTxtById.apply(id))
                        ))),

//              log.debug("get http://localhost:8080/txt-file/{}", fileName);
                pathPrefix("txt-file", () ->
                        path(fileName -> route(
                                getFromResource("web/" + fileName + "." + SomeTxtServiceImpl.OUT_EXT)
                        ))),

//              log.debug("get http://localhost:8080/txt-level-logs");
                pathPrefix("txt-level-logs", () ->
                        pathEndOrSingleSlash(() -> route(
                                get(() -> findSomeTxtLevelAll())
                        ))),

//              log.debug("get http://localhost:8080/txt-level-logs/{}", level);
                pathPrefix("txt-level-logs", () ->
                        path(level -> route(
                                get(() -> findSomeTxtLevelByLevel.apply(level))
                        )))
        );
    }
}
