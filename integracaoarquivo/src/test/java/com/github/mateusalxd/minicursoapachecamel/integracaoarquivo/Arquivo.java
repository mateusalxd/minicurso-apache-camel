package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Arquivo {
    public static String lerConteudo(String nome) throws URISyntaxException, IOException {
        return Files.readString(Path.of(Arquivo.class.getClassLoader().getResource(nome).toURI()));
    }
}
