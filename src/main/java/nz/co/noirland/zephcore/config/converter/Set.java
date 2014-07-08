package nz.co.noirland.zephcore.config.converter;

import nz.co.noirland.zephcore.config.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;

public class Set extends Converter {
    public Set(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        java.util.Set<Object> values = (java.util.Set<Object>) obj;
        java.util.List newList = new ArrayList();

        for (Object val : values) {
            Converter converter = this.converter.getConverter(val.getClass());

            if (converter != null)
                newList.add(converter.toConfig(val.getClass(), val, null));
            else
                newList.add(val);
        }

        return newList;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        java.util.List<Object> values = (java.util.List<Object>) section;
        java.util.Set<Object> newList = new HashSet<Object>();

        try {
            newList = (java.util.Set<Object>) type.newInstance();
        } catch (Exception ignored) { }

        for (Object val : values) {
            Converter converter = this.converter.getConverter(val.getClass());

            if (converter != null)
                newList.add(converter.toConfig(val.getClass(), val, null));
            else
                newList.add(val);
        }

        return newList;
    }

    @Override
    public boolean supports(Class<?> type) {
        return java.util.Set.class.isAssignableFrom(type);
    }

}
