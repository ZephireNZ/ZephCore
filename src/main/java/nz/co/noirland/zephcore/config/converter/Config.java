package nz.co.noirland.zephcore.config.converter;

import nz.co.noirland.zephcore.config.ConfigSection;
import nz.co.noirland.zephcore.config.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Config extends Converter {

    public Config(InternalConverter converter) {
        super(converter);
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        return (obj instanceof Map) ? obj : ((nz.co.noirland.zephcore.config.Config) obj).saveToMap();
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        nz.co.noirland.zephcore.config.Config obj = (nz.co.noirland.zephcore.config.Config) type.newInstance();
        obj.loadFromMap((section instanceof Map) ? (Map) section : ((ConfigSection) section).getRawMap());
        return obj;
    }

    @Override
    public boolean supports(Class<?> type) {
        return nz.co.noirland.zephcore.config.Config.class.isAssignableFrom(type);
    }
}
