package com.ajax_systems.akka.dao;

import com.ajax_systems.akka.model.SomeTxt;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;


public class SomeTxtRepository {

    private final ConcurrentHashMap<String, SomeTxt> someTxts = new ConcurrentHashMap<>();

    {
        String id = "UNKNOWN";
        someTxts.put(id,
                new SomeTxt()
                        .level(id)
                        .total(0));
    }

    public Collection<SomeTxt> find() {
        return someTxts.values();
    }

    public SomeTxt findOne(String id) {
        return someTxts.get(id);
    }

    public SomeTxt save(SomeTxt someTxt) {
        String id = someTxt.getLevel();

        SomeTxt someTxtWithLevel = new SomeTxt()
                .level(someTxt.getLevel())
                .total(someTxt.getTotal());
        someTxts.put(id, someTxtWithLevel);
        return someTxtWithLevel;
    }

    public void update(String id, SomeTxt someTxt) {
        someTxts.put(id, someTxt);
    }

    public void delete(String id) {
        someTxts.remove(id);
    }
}
