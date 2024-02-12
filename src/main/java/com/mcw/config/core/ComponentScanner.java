package com.mcw.config.core;

import com.mcw.config.annotation.ComponentScan;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 自定义组件扫描器，用于在给定基础包中扫描带有特定注解的类。
 */
public class ComponentScanner {

    /**
     * 从配置类上的 @ComponentScan 注解中获取基础包路径，并扫描带有特定注解的类。
     * @param configClass  配置类，带有 @ComponentScan 注解
     * @param annotation   要在类上查找的注解
     * @return 带有指定注解的类列表
     */
    public static List<Class<?>> scan(Class<?> configClass, Class<? extends Annotation> annotation) {
        // 用于存储带有指定注解的类的列表
        List<Class<?>> classes = new ArrayList<>();

        // 获取配置类上的 @ComponentScan 注解
        ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
        if (componentScanAnnotation != null) {
            // 从注解中获取基础包路径
            String[] basePackages = componentScanAnnotation.basePackages();

            // 遍历基础包路径，扫描带有指定注解的类
            for (String basePackage : basePackages) {
                classes.addAll(scanPackage(basePackage, annotation));
            }
        }

        return classes;
    }

    private static List<Class<?>> scanPackage(String basePackage, Class<? extends Annotation> annotation) {
        // 用于存储带有指定注解的类的列表
        List<Class<?>> classes = new ArrayList<>();

        // 转换基础包名称为相应的路径
        String path = basePackage.replace(".", "/");
        try {
            // 获取指定包的所有资源（目录或JAR文件）
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

            // 遍历资源
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                classes.addAll(findClasses(resource, basePackage, annotation));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static List<Class<?>> findClasses(URL directory, String packageName, Class<? extends Annotation> annotation) throws Exception {
        // 用于存储带有指定注解的类的列表
        List<Class<?>> classes = new ArrayList<>();

        // 获取目录对应的文件对象
        File directoryFile = new File(directory.getFile());

        // 如果目录存在
        if (directoryFile.exists()) {
            // 获取目录下的所有文件
            File[] files = directoryFile.listFiles();

            // 遍历文件
            if (files != null) {
                for (File file : files) {
                    // 如果是目录，则递归查找
                    if (file.isDirectory()) {
                        classes.addAll(findClasses(new URL(directory + "/" + file.getName()), packageName + "." + file.getName(), annotation));
                    } else if (file.getName().endsWith(".class")) {
                        // 如果是以 .class 结尾的文件，则加载类
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);

                        // 如果类上带有指定注解，则添加到列表中
                        if (clazz.isAnnotationPresent(annotation)) {
                            classes.add(clazz);
                        }
                    }
                }
            }
        }

        return classes;
    }
}
