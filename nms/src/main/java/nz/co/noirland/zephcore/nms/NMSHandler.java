package nz.co.noirland.zephcore.nms;

import java.util.UUID;

public interface NMSHandler {
    public abstract String getName(UUID uuid);

    public abstract void saveName(UUID uuid, String name);
}
