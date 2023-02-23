package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.support.builder.Namespaces;

import java.io.IOException;

public class IntegracaoArquivo extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        var ns = new Namespaces("ns", "http://www.portalfiscal.inf.br/nfe");

        errorHandler(deadLetterChannel("file:{{diretorioTransportadora1Erro}}"));

        onException(GenericFileOperationFailedException.class)
                .useOriginalMessage()
                .handled(true)
                .maximumRedeliveries(2);

        from("file:{{diretorioEntrada}}?delay=5000")
                .routeId("integracao-arquivo")
                .log("Processando o arquivo: ${file:name}")
                .setProperty("CNPJ", xpath("{{xpathCnpjTransportadora}}", ns))
                .choice()
                .when(exchangeProperty("CNPJ").isEqualTo("1")).to("direct:integracaoTransportadora1")
                .when(exchangeProperty("CNPJ").isEqualTo("2")).to("direct:integracaoTransportadora2")
                .otherwise().log("Transportadora não integrada")
                .end()
                .log("Processamento do arquivo ${file:name} foi concluído");
    }
}
