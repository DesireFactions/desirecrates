package com.desiremc.crates.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.SessionUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.core.utils.cache.RemovalNotification.Cause;
import com.desiremc.crates.DesireCrates;

public class CrateHandler extends BasicDAO<Crate, Integer>
{

    protected static final String META = "crate_id";

    private static CrateHandler instance;

    private static Map<Integer, Crate> crates;

    private static Cache<UUID, Long> breaking;

    private int nextId;

    public CrateHandler()
    {
        super(DesireCore.getInstance().getMongoWrapper().getDatastore());

        crates = new HashMap<>();
        breaking = new Cache<>(100, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                if (entry.getCause() == Cause.EXPIRE)
                {
                    DesireCrates.getLangHandler().sendRenderMessage(Bukkit.getPlayer(entry.getKey()), "breaking.expire");
                }
            }
        }, DesireCrates.getInstance());
    }

    public static Map<Integer, Crate> getCratesMap()
    {
        return crates;
    }

    public static Collection<Crate> getCrates()
    {
        return crates.values();
    }

    public static Crate getCrate(ItemStack item)
    {
        if (item != null && item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().endsWith("Crate"))
        {
            for (Crate crate : crates.values())
            {
                if (item.getItemMeta().getDisplayName().matches(".{0,}" + crate.getName()))
                {
                    return crate;
                }
            }
        }
        return null;
    }

    public static Crate getCrate(Block block)
    {
        if (block != null && block.getType() == Material.CHEST && block.hasMetadata(META))
        {
            return crates.get(block.getMetadata(META).get(0).asInt());
        }
        return null;
    }

    public static Crate getCrate(String name)
    {
        for (Crate crate : getCrates())
        {
            if (crate.getStub().equals(name.toLowerCase()))
            {
                return crate;
            }
        }
        return null;
    }

    public static Crate getCrate(int id)
    {
        return crates.get(id);
    }

    public static Crate createCrate(String name)
    {
        Crate crate = new Crate();
        crate.assignDefaults(name);
        getInstance().save(crate);
        return crate;
    }

    public static void deleteCrate(Crate crate)
    {
        crates.remove(crate.getId());
        crate.unloadHolograms();
        crate.setActive(false);
        saveCrate(crate);
    }

    public static void restoreCrate(Crate crate)
    {
        crates.put(crate.getId(), crate);
        crate.setActive(true);
        crate.loadLocations();
        saveCrate(crate);
    }

    public static Crate getHistoricalCrate(String name)
    {
        return getInstance().createQuery()
                .field("active").equal(false)
                .field("name").equal(name).get();
    }

    public static org.mongodb.morphia.Key<Crate> saveCrate(Crate crate)
    {
        return getInstance().save(crate);
    }

    /**
     * @param uuid the player to toggle
     * @return the new breaking state of the player
     */
    public static boolean toggleBreaking(UUID uuid)
    {
        if (breaking.containsKey(uuid))
        {
            breaking.remove(uuid);
            return false;
        }
        else
        {
            breaking.put(uuid, System.currentTimeMillis());
            return true;
        }
    }

    public static boolean isBreaking(UUID uuid)
    {
        if (!SessionUtils.getRank(uuid).isManager())
        {
            return false;
        }
        return breaking.containsKey(uuid);
    }

    public static void stopBreaking(UUID uuid)
    {
        breaking.remove(uuid);
    }

    public static boolean initialize()
    {
        instance = new CrateHandler();
        try
        {
            for (Crate crate : getInstance().createQuery().field("active").equal(true))
            {
                crate.getKey().setCrate(crate);
                crate.getRewards().forEach(r -> r.setCrate(crate));
                crates.put(crate.getId(), crate);
                if (crate.getId() > getInstance().nextId)
                {
                    getInstance().nextId = crate.getId();
                }
                crate.loadLocations();
            }
            getInstance().nextId++;
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }

    public static CrateHandler getInstance()
    {
        return instance;
    }

}
