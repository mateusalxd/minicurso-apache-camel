package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.builder.Namespaces;

import static org.apache.camel.Exchange.CONTENT_TYPE;

public class ServidorHttp extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        var ns = new Namespaces("ns", "http://www.portalfiscal.inf.br/nfe");

        restConfiguration().host("0.0.0.0").port(8080);

        from("rest:post:nfes")
                .routeId("servidor-http")
                .setProperty("CNPJ", xpath("{{xpathCnpjTransportadora}}", ns))
                .setProperty("Pedido", xpath("{{xpathPedido}}", ns))
                .log("Servidor - CNPJ: ${exchangeProperty.CNPJ}; Pedido: ${exchangeProperty.Pedido};")
                .setHeader(CONTENT_TYPE, constant("application/json"))
                .setBody(constant("{\"status\":\"recebia\"}"));
    }
}
