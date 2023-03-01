package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.main.junit5.CamelMainTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@CamelMainTest(
        mainClass = Main.class,
        replaceRouteFromWith = {"integracao-arquivo=direct:inicio"},
        mockEndpointsAndSkip = "(direct:integracaoTransportadora[12]|file:transportatora1-erro.*)"
)
class IntegracaoArquivoTest {

    @BeanInject
    FluentProducerTemplate template;

    @EndpointInject("mock:direct:integracaoTransportadora1")
    MockEndpoint mockTransportadora1;

    @EndpointInject("mock:direct:integracaoTransportadora2")
    MockEndpoint mockTransportadora2;

    @EndpointInject("mock:file:transportatora1-erro")
    MockEndpoint mockErroFile;

    @Test
    public void should_send_to_correct_integration() throws URISyntaxException, IOException, InterruptedException {
        var body = Arquivo.lerConteudo("transportadora2.xml");

        mockTransportadora1.expectedMessageCount(0);
        mockTransportadora2.expectedMessageCount(0);
        mockErroFile.expectedMessageCount(1);

//        mockTransportadora2.returnReplyBody(new Expression() {
//            @Override
//            public <T> T evaluate(Exchange exchange, Class<T> type) {
//                return (T) "teste";
//            }
//        });
//
//        mockTransportadora2.whenAnyExchangeReceived(new Processor() {
//            @Override
//            public void process(Exchange exchange) throws Exception {
//                exchange.getMessage().setBody("");
//            }
//        });

        var exchange = template.withDefaultEndpoint("direct:inicio")
                .withBody("body")
                .withHeader("CamelFileName", "teste.xml")
                .send();

        mockTransportadora1.assertIsSatisfied();
        mockTransportadora2.assertIsSatisfied();
        mockErroFile.assertIsSatisfied();
    }

}