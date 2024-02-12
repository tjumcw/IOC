import com.mcw.config.core.Container;

/**
 * 采用这种方法，对象的初始化和方法的调用可以基于字符串和注解提供的配置信息进行
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Container container = new Container();
        container.init();
//        // 配置化，消除所有硬编码
//        Properties properties = new Properties();
//        properties.load(Main.class.getResourceAsStream("bean.properties"));
//        String className = properties.getProperty("className");
//        String fieldName = properties.getProperty("fieldName");
//        Class<?> clazz = Class.forName(className);
//        Object instance = container.createInstance(clazz);
//        Field field = clazz.getDeclaredField(fieldName);
//        field.setAccessible(true);
//        // 拿到了customer实例（即一个注入的Customer对象）
//        Object fieldValue = field.get(instance);
//        Method[] methods = fieldValue.getClass().getDeclaredMethods();
//        for (Method method : methods) {
//            // 通过在方法上加注解，可以控制是否调用
//            if (method.getDeclaredAnnotation(Printable.class) != null) {
//                method.invoke(fieldValue);
//            }
//        }
        container.printInfo();
    }
}
