import annotation.Hi;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ApplicationContext {
    private static Map<String, Object> map = new HashMap<>();
    static {
        try {
            load(getAllClassesFrom("classes"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        hiAnnotation();
    }


    public static void load(List<Class<?>> classes) throws Exception {
        for(Class c : classes){
            if (c.isAnnotationPresent(Hi.class)){
                Constructor<?> constructor = c.getConstructor();
                Object object = constructor.newInstance();
                map.put(c.getName(), object);
            }
        }
    }

    public Object getBean(String beanName){
        return map.get("classes." + beanName);
    }

    public static void hiAnnotation(){
        map.keySet().stream().forEach(s -> {
            System.out.println("Я аннотация. Привет всем!");
        });
    }

    private static List<Class<?>> getAllClassesFrom(String packageName) {
        return new Reflections(packageName, new SubTypesScanner(false))
                .getAllTypes()
                .stream()
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
