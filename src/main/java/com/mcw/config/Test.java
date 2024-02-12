package com.mcw.config;

import com.mcw.config.annotation.Component;
import com.mcw.config.annotation.ComponentScan;
import com.mcw.config.annotation.Value;
import com.mcw.config.core.ComponentScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

@ComponentScan(basePackages = {"com.mcw.config.common", "com.mcw.config.service"})
public class Test {
    public static void main(String[] args) throws Exception {

    }
}
