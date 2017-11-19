package com.desiremc.crates.data;

import java.util.Collection;
import java.util.Map;

import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.core.DesireCore;

public class CrateHandler extends BasicDAO<Crate, Integer>
{

    private static CrateHandler instance;

    private static Map<String, Crate> crates;

    private int nextId;

    public CrateHandler()
    {
        super(DesireCore.getInstance().getMongoWrapper().getDatastore());

    }

    public static Map<String, Crate> getCratesMap()
    {
        return crates;
    }

    public static Collection<Crate> getCrates()
    {
        return crates.values();
    }

    public static Crate getCrate(String name)
    {
        return crates.get(name);
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
        crates.remove(crate.getName().toLowerCase());
        crate.unloadHolograms();
        crate.setActive(false);
        saveCrate(crate);
    }

    public static void restoreCrate(Crate crate)
    {
        crates.put(crate.getName().toLowerCase(), crate);
        crate.setActive(true);
        crate.loadHolograms();
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

    public static boolean initialize()
    {
        instance = new CrateHandler();
        try
        {
            for (Crate crate : getInstance().createQuery().field("active").equal(true))
            {
                crate.getKey().setCrate(crate);
                crate.getRewards().forEach(r -> r.setCrate(crate));
                crates.put(crate.getName().toLowerCase(), crate);
                if (crate.getId() > getInstance().nextId)
                {
                    getInstance().nextId = crate.getId();
                }
                crate.loadHolograms();
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
