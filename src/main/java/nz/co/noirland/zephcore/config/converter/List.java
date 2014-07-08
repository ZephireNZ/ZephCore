package nz.co.noirland.zephcore.config.converter;

import nz.co.noirland.zephcore.config.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class List extends Converter {
    public List(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        java.util.List values = (java.util.List) obj;
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
        java.util.List newList = new ArrayList();
        try {
            newList = ((java.util.List) type.newInstance());
        } catch (Exception ignored) {}

        java.util.List values = (java.util.List) section;

        if (genericType.getActualTypeArguments()[0] instanceof Class) {
            Converter converter = this.converter.getConverter((Class) genericType.getActualTypeArguments()[0]);

            if (converter != null) {
                for (Object value : values) {
                    newList.add(converter.fromConfig((Class) genericType.getActualTypeArguments()[0], value, null));
                }
            } else {
                newList = values;
            }
        } else {
            newList = values;
        }

        return newList;
    }

    @Override
    public boolean supports(Class<?> type) {
        return java.util.List.class.isAssignableFrom(type);
    }
}
