package com.ajax_systems.akka;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ajax_systems.akka.web.route.SomeTxtRoute;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.concurrent.ExecutionException;


public class AppProcessor extends AbstractModule {

    private static final Config config = ConfigFactory.load();

    public static void main(String... args) throws InterruptedException, ExecutionException {

        Injector injector = Guice.createInjector(new AppProcessor());

        injector.getInstance(SomeTxtRoute.class)
                .startServer(config.getString("csvRoute.host"), config.getInt("csvRoute.port"));
    }

    @Override
    protected void configure() {
        bind(SomeTxtRoute.class);
    }
}
