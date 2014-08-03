package nz.co.noirland.zephcore.nms.v1_7_R3;

import net.minecraft.server.v1_7_R3.MinecraftServer;
import net.minecraft.server.v1_7_R3.UserCache;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import nz.co.noirland.zephcore.nms.NMSHandler;
import org.bukkit.Bukkit;

import java.util.UUID;

public class NMS implements NMSHandler {

    @Override
    public void saveName(UUID uuid, String name) {
        UserCache cache = ((MinecraftServer) Bukkit.getServer()).getUserCache();
        GameProfile profile = new GameProfile(uuid, name);
        cache.a(profile);
    }

    @Override
    public String getName(UUID uuid) {
        UserCache cache = ((MinecraftServer) Bukkit.getServer()).getUserCache();
        GameProfile profile = cache.a(uuid);
        return profile == null ? null : profile.getName();
    }
}
