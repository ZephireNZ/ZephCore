package nz.co.noirland.zephcore.config.converter;

import nz.co.noirland.zephcore.config.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Primitive extends Converter {
    private HashSet<String> types = new HashSet<String>() {{
        add("boolean");
        add("char");
        add("byte");
        add("short");
        add("int");
        add("long");
        add("float");
        add("double");
    }};

    public Primitive(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        return obj;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        String s = type.getSimpleName();
        if (s.equals("short")) {
            return (section instanceof Short) ? section : ((Integer) section).shortValue();
        } else if (s.equals("byte")) {
            return (section instanceof Byte) ? section : ((Integer) section).byteValue();
        } else if (s.equals("float")) {
            return (section instanceof Float) ? section : ((Double) section).floatValue();
        } else if (s.equals("char")) {
            return (section instanceof Character) ? section : ((String) section).charAt(0);
        } else {
            return section;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return types.contains(type.getName());
    }
}
