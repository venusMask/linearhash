package org.venus.linearhash.args;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.venus.linearhash.core.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author venus
 * @Date 2024/8/15
 * @Version 1.0
 */
public class Argument {

    private final static Logger logger = LoggerFactory.getLogger(Argument.class);

    @Option(usage = "Configuration file path", name = "-config")
    public String configFilePath = "/Users/dzh/software/java/projects/linearhash/config/config.properties";

    public void parse(String[] args) {
        logger.info("The parameters received by Application are {}", Arrays.toString(args));
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseConfig() {
        Properties properties = new Properties();
        logger.info("Starting to parse the configuration file, the configuration file path is: {}", configFilePath);
        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
            Configuration.parseFromProperty(properties);
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
