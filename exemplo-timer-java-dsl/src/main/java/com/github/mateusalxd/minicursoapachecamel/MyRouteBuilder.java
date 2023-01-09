package com.github.mateusalxd.minicursoapachecamel;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:exibirDataHora?period=5000")
                .routeId("rota-java")
                .log("Data e hora: ${date:now:dd/MM/YYYY HH:mm:ss}");
    }
}
