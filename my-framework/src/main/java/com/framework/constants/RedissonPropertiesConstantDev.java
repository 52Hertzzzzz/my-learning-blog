package com.framework.constants;

public class RedissonPropertiesConstantDev extends RedissonPropertiesConstant {

    public static final String masterNode = "redis://127.0.0.1:6379";

    public static final String slaveNode = "redis://127.0.0.1:6379";

    public static final int maxPoolSize = 6;

    public static final int minIdleSize = 3;

}
