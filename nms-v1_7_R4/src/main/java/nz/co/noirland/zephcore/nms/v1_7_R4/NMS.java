package nz.co.noirland.zephcore.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.UserCache;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import nz.co.noirland.zephcore.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;

import java.util.UUID;

public class NMS implements NMSHandler {

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

    @Override
    public String getName(UUID uuid) {
        GameProfile profile = getCache().a(uuid);
        return profile == null ? null : profile.getName();
    }

    private UserCache getCache() {
        return ((CraftServer) Bukkit.getServer()).getServer().getUserCache();
    }
}
