package com.ajax_systems.akka.service.impl;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import com.ajax_systems.akka.dao.SomeTxtLevelRepository;
import com.ajax_systems.akka.service.dto.SomeTxtLevelDTO;
import com.ajax_systems.akka.service.mapper.SomeTxtLevelMapper;
import com.ajax_systems.akka.service.mapper.SomeTxtMapper;
import com.ajax_systems.akka.service.mapper.impl.SomeTxtLevelMapperImpl;
import com.ajax_systems.akka.dao.SomeTxtRepository;
import com.ajax_systems.akka.model.SomeTxt;
import com.ajax_systems.akka.model.SomeTxtLevel;
import com.ajax_systems.akka.service.SomeTxtService;
import com.ajax_systems.akka.service.dto.SomeTxtDTO;
import com.ajax_systems.akka.service.mapper.impl.SomeTxtMapperImpl;
import com.ajax_systems.akka.web.util.PrintUtil;
import com.ajax_systems.akka.web.util.SomeTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletionStage;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


public class SomeTxtServiceImpl implements SomeTxtService {

    private static final Logger log = LoggerFactory.getLogger(SomeTxtServiceImpl.class);

    private static final int MAXIMUM_FRAME_LENGTH = 256;

    public static final String OUT_PATH = "src/main/resources/web";

    public static final String OUT_EXT = "txt";

    private SomeTxtRepository someTxtRepository;

    private SomeTxtLevelRepository someTxtLevelRepository;

    private SomeTxtMapper someTxtMapper;

    private SomeTxtLevelMapper someTxtLevelMapper;

    private ActorSystem system;

    private volatile Collection<SomeTxtLevel> someTxtLevelCache = new ArrayList<>();

    /**
     *
     */
    private Flow<ByteString, String, NotUsed> splitFlow = Framing
            .delimiter(ByteString.fromString("\n"), MAXIMUM_FRAME_LENGTH, FramingTruncation.ALLOW)
            .map(byteString -> byteString.decodeString(ByteString.UTF_8()));

    /**
     *
     */
    private Source<SomeTxtLevel, Object> txtSource;

    private Source<SomeTxtLevel, NotUsed> memoSource;

    /**
     *
     */
    private Sink<SomeTxtLevel, CompletionStage<Done>> logProcess = Sink.foreach(bCast -> log.info(bCast.toString()));

    private Sink<SomeTxtLevel, CompletionStage<Done>> consoleProcess = Sink.foreach(PrintUtil::print);

    private Sink<SomeTxtLevel, CompletionStage<Done>> cacheProcess = Sink.foreach(getSomeTxtLevelCache()::add);

    private Sink<SomeTxtLevel, CompletionStage<Done>> memoProcess = Sink.foreach(bCast -> {
                int total = someTxtLevelRepository.findByLevel(bCast.getLevel()).size() + 1;
                someTxtRepository.save(new SomeTxt().level(bCast.getLevel()).total(total));
                someTxtLevelRepository.save(bCast);
            } );

    private Sink<SomeTxtLevel, CompletionStage<Done>> fileProcess = Sink.foreach(bCast -> {
        String outPath = OUT_PATH + "/" + bCast.getLevel() + "." + OUT_EXT;
        try (FileWriter prWr = new FileWriter(new File(outPath), true)) {
            prWr.write(bCast.getDescription() + '\n');
        }
    });

    @Inject
    public SomeTxtServiceImpl(SomeTxtLevelRepository someTxtLevelRepository,
                              SomeTxtRepository someTxtRepository,
                              SomeTxtMapperImpl someTxtMapper,
                              SomeTxtLevelMapperImpl someTxtLevelMapper) {
        this.someTxtLevelRepository = someTxtLevelRepository;
        this.someTxtRepository = someTxtRepository;
        this.someTxtMapper = someTxtMapper;
        this.someTxtLevelMapper = someTxtLevelMapper;
    }

    @Override
    public SomeTxtService init(ActorSystem system) {
        this.system = system;
        return this;
    }

    @Override
    public void upload(Source<ByteString, Object> data) {
        this.txtSource = data
                .via(splitFlow)
                .map(description -> {
                    String level = matchLevel(description);
                    int size = description.length();
                    return new SomeTxtLevel()
                            .level(level)
                            .description(description)
                            .size(size);
                });

        RunnableGraph.fromGraph(doUploadGraph()).run(ActorMaterializer.create(system));
    }

    @Override
    public SomeTxtLevelDTO findSomeTxtLevelOne(String id) {
        SomeTxtLevel result = someTxtLevelRepository.findOne(id);
        return someTxtLevelMapper.toDto(result);
    }

    @Override
    public Collection<SomeTxtLevelDTO> findSomeTxtLevel() {
        memoSource = Source.from(new FindSource());
        RunnableGraph.fromGraph(getFromCacheGraph()).run(ActorMaterializer.create(system));
        return getSomeTxtLevelCache().stream()
                .map(someTxtLevelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<SomeTxtLevelDTO> findSomeTxtLevelByLevel(String level) {
        memoSource = Source.from(new FindByLevelSource(level));
        RunnableGraph.fromGraph(getFromCacheGraph()).run(ActorMaterializer.create(system));
        return getSomeTxtLevelCache().stream()
                .map(someTxtLevelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<SomeTxtDTO> findSomeTxt() {
        Collection<SomeTxt> result = someTxtRepository.find();
        return result.stream()
                .map(someTxtMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SomeTxtDTO findSomeTxtOne(String id) {
        SomeTxt result = someTxtRepository.findOne(id);
        return someTxtMapper.toDto(result);
    }

    private Graph<ClosedShape, CompletionStage<Done>> doUploadGraph() {
        return GraphDSL.create(logProcess, (builder, sink) -> {
            UniformFanOutShape<SomeTxtLevel, SomeTxtLevel> broadcast = builder.add(Broadcast.create(3));
            SourceShape<SomeTxtLevel> source = builder.add(txtSource);
            SinkShape<SomeTxtLevel> fileSink = builder.add(fileProcess);
            SinkShape<SomeTxtLevel> memoSink = builder.add(memoProcess);

            builder.from(source).viaFanOut(broadcast).to(sink)
                    .from(broadcast).to(fileSink)
                    .from(broadcast).to(memoSink);

            return ClosedShape.getInstance();
        });
    }

    private Graph<ClosedShape, CompletionStage<Done>> getFromCacheGraph() {
        getSomeTxtLevelCache().clear();
        return GraphDSL.create(logProcess, (builder, sink) -> {
            UniformFanOutShape<SomeTxtLevel, SomeTxtLevel> broadcast = builder.add(Broadcast.create(3));
            SourceShape<SomeTxtLevel> source = builder.add(memoSource);
            SinkShape<SomeTxtLevel> consoleSink = builder.add(consoleProcess);
            SinkShape<SomeTxtLevel> cacheSink = builder.add(cacheProcess);

            builder.from(source).viaFanOut(broadcast).to(sink)
                    .from(broadcast).to(consoleSink)
                    .from(broadcast).to(cacheSink);

            return ClosedShape.getInstance();
        });
    }

    private Collection<SomeTxtLevel> getSomeTxtLevelCache() {
        return someTxtLevelCache;
    }

    private String matchLevel(String description) {
        Matcher matcher = SomeTxtUtil.PATTERN.matcher(description);
        if (matcher.find())
            return matcher.group(1);
        else
            return "UNKNOWN";
    }


    /**
     *
     */
    private class FindSource implements Iterable<SomeTxtLevel> {

        @Override
        public Iterator<SomeTxtLevel> iterator() {
            return new Iterator<SomeTxtLevel>() {

                private Iterator<SomeTxtLevel> iSomeTxtLevel = someTxtLevelRepository.find().iterator();

                @Override
                public boolean hasNext() {
                    return iSomeTxtLevel.hasNext();
                }

                @Override
                public SomeTxtLevel next() {
                    return iSomeTxtLevel.next();
                }
            };
        }
    }

    private class FindByLevelSource implements Iterable<SomeTxtLevel> {

        private final String level;

        FindByLevelSource(String level) {
            this.level = level;
        }

        @Override
        public Iterator<SomeTxtLevel> iterator() {
            return new Iterator<SomeTxtLevel>() {

                private Iterator<SomeTxtLevel> iSomeTxtLevel = someTxtLevelRepository.findByLevel(level).iterator();

                @Override
                public boolean hasNext() {
                    return iSomeTxtLevel.hasNext();
                }

                @Override
                public SomeTxtLevel next() {
                    return iSomeTxtLevel.next();
                }
            };
        }
    }
}
