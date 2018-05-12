package com.lynn.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {

    /**
     * 标识版本号
     * @return
     */
    int value();
}
