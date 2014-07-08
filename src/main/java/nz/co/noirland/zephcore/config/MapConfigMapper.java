package nz.co.noirland.zephcore.config;

import nz.co.noirland.zephcore.config.converter.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class MapConfigMapper extends YamlConfigMapper {
    public Map saveToMap() throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        for (Field field : getClass().getDeclaredFields()) {
            if (doSkip(field)) continue;

            String path = (CONFIG_MODE.equals(ConfigMode.DEFAULT)) ? field.getName().replaceAll("_", ".") : field.getName();

            if (field.isAnnotationPresent(Path.class)) {
                Path path1 = field.getAnnotation(Path.class);
                path = path1.value();
            }

            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }

            try {
                returnMap.put(path, field.get(this));
            } catch (IllegalAccessException ignored) { }
        }

        Converter mapConverter = converter.getConverter(Map.class);
        return (Map) mapConverter.toConfig(HashMap.class, returnMap, null);
    }

    public void loadFromMap(Map section) throws Exception {
        for (Field field : getClass().getDeclaredFields()) {
            if (doSkip(field)) continue;

            String path = (CONFIG_MODE.equals(ConfigMode.DEFAULT)) ? field.getName().replaceAll("_", ".") : field.getName();

            if (field.isAnnotationPresent(Path.class)) {
                Path path1 = field.getAnnotation(Path.class);
                path = path1.value();
            }

            if(Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }

            converter.fromConfig((Config) this, field, ConfigSection.convertFromMap(section), path);
        }
    }
}
