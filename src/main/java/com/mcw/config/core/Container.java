package com.mcw.config.core;

import com.mcw.config.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册服务，获得实例
 */
@ComponentScan(basePackages = {"com.mcw.config"})
public class Container {
    
    // 存的是方法本身，通过invoke获取对象实例，不直接存对象实例
    private Map<Class<?>, Method> methods;

    // 存的是各种实例化的bean（根据类型）
    private Map<Class<?>, Object> unNamedBeans;

    // 存的是各种实例化的bean（根据名字）
    private Map<String, Object> namedBeans;

    // 存的是各种配置类（加了@configuration注解的类）
    private Map<Class<?>, Object> configs;

    // 用来存放Bean注解下依赖的bean所对应的@Configuration的配置类
    private Map<Class<?>, Class<?>> classMap;

    public void printInfo() {
        System.out.println("unNamedBeans: " + this.unNamedBeans);
        System.out.println("namedBeans: " + this.namedBeans);
    }

    public void init() throws Exception {
        this.methods = new HashMap<>();
        this.unNamedBeans = new HashMap<>();
        this.namedBeans = new HashMap<>();
        this.configs = new HashMap<>();
        this.classMap = new HashMap<>();
        initConfigurations();
        initComponents();
    }

    /**
     * 初始化各种配置类，把带@Bean注解的方法存到map
     * @throws Exception
     */
    private void initConfigurations() throws Exception {
        List<Class<?>> configClasses = ComponentScanner.scan(Container.class, Configuration.class);
        for (Class<?> clazz : configClasses) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getDeclaredAnnotation(Bean.class) != null) {
                    this.methods.put(method.getReturnType(), method);
                    this.classMap.put(method.getReturnType(), clazz);
                }
            }
            // 初始化一个Config对象的实例，用于其method通过invoke调用方法
            if (!this.configs.containsKey(clazz)) {
                this.configs.put(clazz, clazz.getConstructor().newInstance());
            }
        }
    }

    /**
     * 初始化所有带@Component注解的bean
     * @throws Exception
     */
    private void initComponents() throws Exception {
        // 获得所有带@Component注解的类
        List<Class<?>> components = ComponentScanner.scan(Container.class, Component.class);
        for (Class<?> clazz : components) {
            System.out.println("class is " + clazz);
            Object obj = createComponent(clazz);
            // 区分是命名的component还是未命名的，放在不同的容器
            Component componentAnnotation = clazz.getAnnotation(Component.class);
            String componentName = componentAnnotation.value();
            if (componentName.isEmpty() && !this.unNamedBeans.containsKey(clazz)) {
                this.unNamedBeans.put(clazz, obj);
            }
            if (!componentName.isEmpty() && !this.namedBeans.containsKey(clazz)) {
                this.namedBeans.put(componentName, obj);
            }
        }
    }

    private Object createComponent(Class<?> clazz) throws Exception {
        Object obj = createInstanceByConstructor(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            /** 如果这个字段带@Value注解，实现值的注入 */
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String filedValue = valueAnnotation.value();
                field.setAccessible(true);
                if (field.getType().getName().equals("java.lang.Integer")) {
                    field.set(obj, Integer.parseInt(filedValue));
                } else {
                    field.set(obj, filedValue);
                }
            }
            /** 如果一个字段带Autowired注解，实现bean的注入 */
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                String autowiredName = autowiredAnnotation.value();
                Object autowiredBean;
                if (autowiredName.isEmpty()) {
                    // 根据类型注入（注入的应该是这个字段的类型）-> 可能存在递归调用
                    autowiredBean = this.unNamedBeans.getOrDefault(field.getType(), createComponent(field.getType()));
                } else {
                    // 根据名字注入-> 可能存在递归调用
                    autowiredBean = this.namedBeans.getOrDefault(autowiredName, createComponent(clazz));
                }
                field.setAccessible(true);
                field.set(obj, autowiredBean);
            }
        }
        return obj;
    }

    /**
     * 通过类的Class对象获取相应的服务实例
     * @param clazz
     * @return
     */
    public Object getServiceInstanceByClass(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        if (this.unNamedBeans.containsKey(clazz)) {
            return this.unNamedBeans.get(clazz);
        }
        if (this.methods.containsKey(clazz)) {
            Method method = this.methods.get(clazz);
            Object obj = method.invoke(this.configs.get(this.classMap.get(clazz)));
            String beanName = method.getDeclaredAnnotation(Bean.class).value();
            if (beanName.isEmpty()) {
                this.unNamedBeans.put(clazz, obj);
            } else {
                this.namedBeans.put(beanName, obj);
            }
            return obj;
        }
        return null;
    }

    /**
     * 通过Clazz对象创建普通实例，并实现将服务自动注入对象
     * @param clazz
     * @return
     */
    public Object createInstanceByConstructor(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getDeclaredAnnotation(Autowired.class) != null) {
                // 获取构造器的参数类型
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                // 通过定义的getServiceInstanceByClass方法获取所有需要的service实例
                Object[] arguments = new Object[parameterTypes.length];
                for (int i = 0; i < arguments.length; i++) {
                    arguments[i] = getServiceInstanceByClass(parameterTypes[i]);
                }
                // 利用service实例参数，反射调用构造器创建对象
                return constructor.newInstance(arguments);
            }
        }
        // 没带注解就通过无参构造获取实例
        return clazz.getConstructor().newInstance();
    }
}
