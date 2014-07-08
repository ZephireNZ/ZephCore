package nz.co.noirland.zephcore.config.converter;


import nz.co.noirland.zephcore.config.InternalConverter;

import java.lang.reflect.ParameterizedType;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Array extends Converter {

    public Array(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        return obj;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        java.util.List values = (java.util.List) section;
        return getArray(type, values);
    }

    private static <T> T[] getArray(Class<T> type, java.util.List list) {
        T[] array = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
        return (T[]) list.toArray(array);
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isArray();
    }
}
