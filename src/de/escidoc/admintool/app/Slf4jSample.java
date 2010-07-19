package de.escidoc.admintool.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jSample {

    public static void main(final String[] args) {
        final Logger logger = LoggerFactory.getLogger(Slf4jSample.class);

        logger.info("Hello World!");
    }

    private Slf4jSample() {
    }

}