package nz.co.noirland.zephcore;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Util {


    public static final long HOUR = TimeUnit.HOURS.toMillis(1);
    public static final long DAY = TimeUnit.DAYS.toMillis(1);

    public static boolean isSign(Block block) {
        return block != null && (block.getState() instanceof Sign);
    }

    public static boolean isSignAttachedToBlock(Block sign, Block block) {
        org.bukkit.material.Sign sData = (org.bukkit.material.Sign) sign.getState().getData();
        Block attached = sign.getRelative(sData.getAttachedFace());
        return attached.equals(block); // Has no trade sign attached
    }

    public static UUID uuid(String name) {
        return player(name).getUniqueId();
    }

    public static OfflinePlayer player(UUID uuid) {
        return ZephCore.inst().getServer().getOfflinePlayer(uuid);
    }

    public static OfflinePlayer player(String name) {
        return ZephCore.inst().getServer().getOfflinePlayer(name);
    }

    public static String formatTime(long millis) {
        if(millis < HOUR) {
            long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
            return (mins + " Minutes");
        }
        else if(millis < DAY) {
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            return (hours + " Hours");
        }
        else{
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long subtrDays = TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(millis - subtrDays);
            return (days + " Days, " + hours + " Hours");
        }
    }

    public static <K> TreeMap<Integer, K> getSortedMap(Set<K> set) {
        TreeMap<Integer, K> map = new TreeMap<Integer, K>();
        Iterator<K> it = set.iterator();
        int i = 1;
        while(it.hasNext()) {
            map.put(i++, it.next());
        }
        return map;
    }
}
