package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.support.builder.Namespaces;

public class IntegracaoArquivo extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        var ns = new Namespaces("ns", "http://www.portalfiscal.inf.br/nfe");

        from("file:{{diretorioEntrada}}?delay=5000")
                .routeId("integracao-arquivo")
                .log("Processando o arquivo: ${file:name}")
                .setProperty("CNPJ", xpath("{{xpathCnpjTransportadora}}", ns))
                .choice()
                .when(exchangeProperty("CNPJ").isEqualTo("1"))
                .to("file:{{diretorioTransportadora1}}?fileName=${date:now:HHmmss}_${file:name}")
                .when(exchangeProperty("CNPJ").isEqualTo("2"))
                    .throttle(1).timePeriodMillis(5000).asyncDelayed()
                        .setHeader(HttpConstants.HTTP_METHOD, constant("POST"))
                        .setHeader(HttpConstants.HTTP_URI, constant("{{urlApiTransportadora2}}"))
                        .setHeader(HttpConstants.HTTP_PATH, constant("nfes"))
                        .setHeader(HttpConstants.CONTENT_TYPE).constant("application/xml")
                        .to("http:servidorTransportadora2")
                        .endChoice()
                .otherwise()
                .log("Transportadora não integrada")
                .end();
    }
}
