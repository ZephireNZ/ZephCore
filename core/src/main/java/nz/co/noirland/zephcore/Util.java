package nz.co.noirland.zephcore;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Util {


    public static final long HOUR = TimeUnit.HOURS.toMillis(1);
    public static final long DAY = TimeUnit.DAYS.toMillis(1);

    private static final Random rand = new Random();
    private static final CancelFallManager fallManager = new CancelFallManager();

    public static boolean isSign(Block block) {
        return block != null && (block.getState() instanceof Sign);
    }

    public static boolean isSignAttachedToBlock(Block sign, Block block) {
        org.bukkit.material.Sign sData = (org.bukkit.material.Sign) sign.getState().getData();
        Block attached = sign.getRelative(sData.getAttachedFace());
        return attached.equals(block); // Has no trade sign attached
    }

    public static UUID uuid(String name) {
        UUID uuid = ZephCore.getNMS().getUUID(name);
        if(uuid == null) {
            uuid = UUIDFetcher.getUUID(name);
            if(uuid != null) {
                ZephCore.getNMS().save(uuid, name);
            }
        }
        return uuid;
    }

    public static OfflinePlayer player(UUID uuid) {
        return ZephCore.inst().getServer().getOfflinePlayer(uuid);
    }

    public static OfflinePlayer player(String name) {
        return ZephCore.inst().getServer().getOfflinePlayer(name);
    }

    public static String name(UUID uuid) {
        String name = player(uuid).getName();
        if(name == null) {
            name = ZephCore.getNMS().getName(uuid);
        }
        if(name == null) {
            try {
                name = NameFetcher.getName(uuid);
                if(name != null) {
                    ZephCore.getNMS().save(uuid, name);
                }
            } catch (Exception e) {
                ZephCore.debug().warning("Could not find name for UUID " + uuid.toString());
            }
        }
        return name;
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

    public static String concatenate(String start, Collection<?> values, String startDelim, String delim) {
        StringBuilder builder = new StringBuilder(start);

        String d = startDelim;
        for(Object str : values) {
            builder.append(d).append(str.toString());
            d = delim;
        }
        return builder.toString();
    }

    public static double round(double in, DecimalFormat format) {
        return Double.parseDouble(format.format(in));
    }

    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static int createRandomHex(int length) {
        return rand.nextInt((int) Math.pow(0x10, length));
    }

    public static ItemStack createItem(String item, String data) {
        Material material = Material.getMaterial(item);
        MaterialData materialData = parseMaterialData(material, data);
        return materialData.toItemStack();
    }

    public static Map<String, Object> toMap(Location loc) {
        if(loc == null) return null;
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("world", loc.getWorld().getName());
        ret.put("x", loc.getX());
        ret.put("y", loc.getY());
        ret.put("z", loc.getZ());
        ret.put("yaw", loc.getYaw());
        ret.put("pitch", loc.getPitch());
        return ret;
    }

    @SuppressWarnings("deprecated")
    public static MaterialData parseMaterialData(Material material, String data) {
        try{
            int nRet = Integer.parseInt(data);
            return new MaterialData(material, (byte) nRet);
        }catch(NumberFormatException ignored) {}
        MaterialData ret;
        switch(material) {
            case WOOD:
            case SAPLING:
            case LOG:
            case LEAVES:
                ret = new Tree(material, TreeSpecies.valueOf(data).getData());
                break;
            //TODO: Change after non-deprecated methods are added
            case LOG_2:
            case LEAVES_2:
                TreeSpecies species = TreeSpecies.valueOf(data);
                switch(species) {
                    case ACACIA:
                    default:
                        ret = new MaterialData(material, (byte) 0x0);
                        break;
                    case DARK_OAK:
                        ret = new MaterialData(material, (byte) 0x1);
                        break;
                }
                break;
            case SANDSTONE:
                ret = new Sandstone(SandstoneType.valueOf(data));
                break;
            case LONG_GRASS:
                ret = new LongGrass(GrassSpecies.valueOf(data));
                break;
            case WOOL:
                ret = new Wool(DyeColor.valueOf(data));
                break;
            //TODO: Change after non-deprecated methods are added
            case STAINED_GLASS:
            case STAINED_GLASS_PANE:
            case STAINED_CLAY:
            case CARPET:
                ret = new MaterialData(material, DyeColor.valueOf(data).getWoolData());
                break;
            case DOUBLE_STEP:
                Material type = Material.valueOf(data);
                switch(type) {
                    default: // STONE
                        ret = new MaterialData(material, (byte) 0x8);
                        break;
                    case SANDSTONE:
                        ret = new MaterialData(material, (byte) 0x9);
                        break;
                    case QUARTZ_BLOCK:
                        ret = new MaterialData(material, (byte) 0xF);
                }
                break;
            case STEP:
                type = Material.valueOf(data);
                switch(type) {
                    default: // STONE
                        ret = new MaterialData(material, (byte) 0x0);
                        break;
                    case SANDSTONE:
                        ret = new MaterialData(material, (byte) 0x1);
                        break;
                    case WOOD:
                        ret = new MaterialData(material, (byte) 0x2);
                        break;
                    case COBBLESTONE:
                        ret = new MaterialData(material, (byte) 0x3);
                        break;
                    case BRICK:
                        ret = new MaterialData(material, (byte) 0x4);
                        break;
                    case SMOOTH_BRICK:
                        ret = new MaterialData(material, (byte) 0x5);
                        break;
                    case NETHER_BRICK:
                        ret = new MaterialData(material, (byte) 0x6);
                        break;
                    case QUARTZ_BLOCK:
                        ret = new MaterialData(material, (byte) 0x7);
                }
                break;
            case SMOOTH_BRICK:
                // STONE for Smooth, MOSSY_COBBLESTONE for mossy, COBBLESTONE for cracked, STONE_BRICK for carved
                ret = new SmoothBrick(Material.valueOf(data));
                break;
            case WOOD_DOUBLE_STEP:
            case WOOD_STEP:
                ret = new WoodenStep(TreeSpecies.valueOf(data));
                break;
            case COAL:
                ret = new Coal(CoalType.valueOf(data));
                break;
            case INK_SACK:
                Dye mat = new Dye();
                mat.setColor(DyeColor.valueOf(data));
                ret = mat;
                break;
            case MONSTER_EGG:
                ret = new SpawnEgg(EntityType.valueOf(data));
                break;
            default:
                ret = new MaterialData(material);
                break;
        }
        return ret;
    }

    @SuppressWarnings("deprecated")
    public static String getMaterialName(MaterialData data) {
        String ret;
        switch(data.getItemType()) {
            case WOOD:
            case SAPLING:
            case LOG:
            case LEAVES:
                ret = ((Tree) data).getSpecies().toString();
                break;
            //TODO: Change after non-deprecated methods are added
            case LOG_2:
            case LEAVES_2:
                switch(data.getData()) {
                    case 0x0:
                    default:
                        ret = TreeSpecies.ACACIA.toString();
                        break;
                    case 0x1:
                        ret = TreeSpecies.DARK_OAK.toString();
                        break;
                }
                break;
            case SANDSTONE:
                ret = ((Sandstone) data).getType().toString();
                break;
            case LONG_GRASS:
                ret = ((LongGrass) data).getSpecies().toString();
                break;
            case WOOL:
                ret = ((Wool) data).getColor().toString();
                break;
            //TODO: Change after non-deprecated methods are added
            case STAINED_GLASS:
            case STAINED_GLASS_PANE:
            case STAINED_CLAY:
            case CARPET:
                ret = DyeColor.getByWoolData(data.getData()).toString();
                break;
            case DOUBLE_STEP:
            case STEP:
                ret = ((Step) data).getMaterial().toString();
                break;
            case SMOOTH_BRICK:
                // STONE for Smooth, MOSSY_COBBLESTONE for mossy, COBBLESTONE for cracked, STONE_BRICK for carved
                ret = ((SmoothBrick) data).getMaterial().toString();
                break;
            case WOOD_DOUBLE_STEP:
            case WOOD_STEP:
                ret = ((WoodenStep) data).getSpecies().toString();
                break;
            case COAL:
                ret = ((Coal) data).getType().toString();
                break;
            case INK_SACK:
                ret = ((Dye) data).getColor().toString();
                break;
            case MONSTER_EGG:
                ret = ((SpawnEgg) data).getSpawnedType().toString();
                break;
            default:
                ret = "";
                break;
        }
        return ret;
    }

    /**
     * Returns a psuedo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimim value
     * @param max Maximim value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Returns a pseudo-random number between 0 (inclusive) and max (exclusive).
     * @param max Max value, exclusive
     * @return A random int
     */
    public static int randInt(int max) {
        return rand.nextInt(max);
    }

    public static <T> T randInArray(T[] array) {
        int index = rand.nextInt(array.length);
        return array[index];
    }

    public static void cancelFall(Player player) {
        boolean isNearGround = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid();
        if(isNearGround) return;

        fallManager.addPlayer(player.getUniqueId());
    }
}
