package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;

public class IntegracaoTransportadora1 extends RouteBuilder {
    @Override
    public void configure() throws Exception {


        from("direct:integracaoTransportadora1")
                .routeId("integracao-arquivo-transportadora1")
                .log("Integrando com a transportadora 1")
                .errorHandler(noErrorHandler())
                .setBody(constant("Um teste"))
                .to("file:{{diretorioTransportadora1}}?fileName=${date:now:HHmmss}_${file:name}");
    }
}
