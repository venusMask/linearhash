package org.venus.linearhash;

import lombok.Getter;

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

    private Configuration() {}

}
