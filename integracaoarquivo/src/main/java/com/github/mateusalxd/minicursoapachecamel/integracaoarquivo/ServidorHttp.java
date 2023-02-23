package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.builder.Namespaces;

import java.util.Random;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

public class ServidorHttp extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        var ns = new Namespaces("ns", "http://www.portalfiscal.inf.br/nfe");

        restConfiguration().host("0.0.0.0").port(8080);

        from("rest:post:nfes")
                .routeId("servidor-http")
                .setProperty("CNPJ", xpath("{{xpathCnpjTransportadora}}", ns))
                .setProperty("Pedido", xpath("{{xpathPedido}}", ns))
                .setHeader(CONTENT_TYPE, constant("application/json"))
                .process(this::constuirResposta)
                .log("Servidor - (número aleatório: ${header.NumeroAleatorio}) - " +
                        "CNPJ: ${exchangeProperty.CNPJ}; " +
                        "Pedido: ${exchangeProperty.Pedido}; " +
                        "Body: ${bodyOneLine}");
    }

    private void constuirResposta(Exchange exchange) {
        var numeroAleatorio = new Random().ints(1, 1, 21).findFirst().getAsInt();
        var mensagem = exchange.getMessage();

        mensagem.setHeader("NumeroAleatorio", numeroAleatorio);

        if (numeroAleatorio > 5 && numeroAleatorio <= 12) {
            mensagem.setHeader(HTTP_RESPONSE_CODE, 429);
            mensagem.setBody("{\"status\":\"erro\",\"mensagem\":\"Muitas requisições\"}");
        } else if (numeroAleatorio > 12) {
            mensagem.setHeader(HTTP_RESPONSE_CODE, 500);
            mensagem.setBody("{\"status\":\"erro\",\"mensagem\":\"Erro interno\"}");
        }
    }
}
