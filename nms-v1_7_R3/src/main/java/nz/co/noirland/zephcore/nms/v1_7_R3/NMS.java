package nz.co.noirland.zephcore.nms.v1_7_R3;

import net.minecraft.server.v1_7_R3.UserCache;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import nz.co.noirland.zephcore.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;

import java.util.UUID;

public class NMS implements NMSHandler {

    @Override
    public void saveName(UUID uuid, String name) {
        GameProfile profile = new GameProfile(uuid, name);
        getCache().a(profile);
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
