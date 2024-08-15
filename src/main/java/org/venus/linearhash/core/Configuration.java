package org.venus.linearhash.core;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Properties;

/**
 * Linear Hash Configuration.
 *
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
@Getter
public class Configuration {

    @Getter
    private static final Configuration instance = new Configuration();

    private Integer maxBucketCap = 16;

    private Integer minBucketCap = 1;

    private Double loadFactory = 0.75d;

    private Integer overflowPoolSize = 8;

    private Integer initBuckets = 1;

    private String dataDir;

    private Configuration() {}

    public static void parseFromProperty(Properties properties)
            throws InvocationTargetException, IllegalAccessException {
        Class<Configuration> aClass = Configuration.class;
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method: methods) {
            ParseConfig parseConfig = method.getAnnotation(ParseConfig.class);
            if(parseConfig != null) {
                method.invoke(Configuration.getInstance(), properties);
            }
        }
    }

    @ParseConfig
    private void parseMaxBucketCap(Properties properties) {
        this.maxBucketCap = (Integer) properties.getOrDefault("max-bucket-cap", 16);
    }

    @ParseConfig
    private void parseMinBucketCap(Properties properties) {
        this.minBucketCap = (Integer) properties.getOrDefault("min-bucket-cap", 2);
    }

    @ParseConfig
    private void parseLoadFactory(Properties properties) {
        this.loadFactory = (Double) properties.getOrDefault("load-factory", 0.75d);
    }

    @ParseConfig
    private void parseInitBuckets(Properties properties) {
        this.initBuckets = (Integer) properties.getOrDefault("init-buckets", 1);
    }

    @ParseConfig
    private void parseOverflowPoolSize(Properties properties) {
        this.overflowPoolSize = (Integer) properties.getOrDefault("overflow-pool-size", 8);
    }

    @ParseConfig
    private void parseDataDir(Properties properties) {
        Object logs = properties.get("logs");
        if(Objects.isNull(logs)) {
            throw new NullPointerException("logs must not be null !");
        }
        this.dataDir = logs.toString();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface ParseConfig { }

}
