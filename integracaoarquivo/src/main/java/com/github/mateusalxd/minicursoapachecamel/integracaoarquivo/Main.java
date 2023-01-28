package com.github.mateusalxd.minicursoapachecamel.integracaoarquivo;

/**
 * Main class that boot the Camel application
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws Exception {
        // use Camels Main class
        org.apache.camel.main.Main main = new org.apache.camel.main.Main(Main.class);
        // now keep the application running until the JVM is terminated (ctrl + c or sigterm)
        main.run(args);
    }

}
