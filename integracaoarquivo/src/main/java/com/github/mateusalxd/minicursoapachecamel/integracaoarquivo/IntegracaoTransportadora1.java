package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.builder.RouteBuilder;

public class IntegracaoTransportadora1 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:integracaoTransportadora1")
                .routeId("integracao-arquivo-transportadora1")
                .to("file:{{diretorioTransportadora1}}?fileName=${date:now:HHmmss}_${file:name}");
    }
}
