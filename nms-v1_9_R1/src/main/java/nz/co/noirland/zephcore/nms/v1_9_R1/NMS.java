package nz.co.noirland.zephcore.nms.v1_9_R1;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_9_R1.UserCache;
import nz.co.noirland.zephcore.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;

import java.util.UUID;

public class NMS implements NMSHandler {

    @Override
    public String getName(UUID uuid) {
        GameProfile profile = getCache().a(uuid);
        return profile == null ? null : profile.getName();
    }

    @Override
    public void save(UUID uuid, String name) {
        getCache().a(new GameProfile(uuid, name));
        getCache().c();
    }

    @Override
    public UUID getUUID(String name) {
        GameProfile profile = getCache().getProfile(name);
        return profile == null ? null : profile.getId();
    }

    private UserCache getCache() {
        return ((CraftServer) Bukkit.getServer()).getServer().getUserCache();
    }
}
