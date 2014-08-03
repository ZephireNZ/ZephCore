package nz.co.noirland.zephcore.nms;

import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.ZephCore;
import org.bukkit.Bukkit;

public abstract class NMSHandler {

    private static NMSHandler inst;

    public static NMSHandler inst() {
        if(inst == null) {
            inst = getNMS();
        }
        return inst;
    }

    private static NMSHandler getNMS() {
        String pName = Bukkit.getServer().getClass().getPackage().getName();
        String ver = pName.substring(pName.lastIndexOf('.') + 1);
        try {
            Class<?> clazz = Class.forName("nz.co.noirland.zephcore.nms." + ver + ".NMS");
            if(NMSHandler.class.isAssignableFrom(clazz)) {
                return (NMSHandler) clazz.getConstructor().newInstance();
            }
            throw new Exception();
        } catch (Exception e) {
            Debug debug = ZephCore.debug();
            debug.disable("Minecraft version " + ver + " unsupported!", e);
            return null;
        }
    }
}
