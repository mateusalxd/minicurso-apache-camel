package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.http.base.HttpOperationFailedException;

public class IntegracaoTransportadora2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(HttpOperationFailedException.class)
                .useOriginalMessage()
                .handled(true)
                .maximumRedeliveries(2)
                .to("file:{{diretorioTransportadora2Erro}}")
                .process(exchange -> {
                    var excecao = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
                    exchange.getMessage().setBody(excecao.getResponseBody());
                })
                .to("file:{{diretorioTransportadora2Erro}}?fileName=${file:name}.erro");

        from("direct:integracaoTransportadora2")
                .routeId("integracao-arquivo-transportadora2")
                .log("Integrando com a transportadora 2")
                .throttle(1).timePeriodMillis(5000).asyncDelayed()
                    .setHeader(HttpConstants.HTTP_METHOD, constant("POST"))
                    .setHeader(HttpConstants.HTTP_URI, constant("{{urlApiTransportadora2}}"))
                    .setHeader(HttpConstants.HTTP_PATH, constant("nfes"))
                    .setHeader(HttpConstants.CONTENT_TYPE).constant("application/xml")
                    .to("http:servidorTransportadora2");
    }
}
