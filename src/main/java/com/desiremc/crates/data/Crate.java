package com.desiremc.crates.data;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.api.LocationTypeConverter;
import com.desiremc.crates.DesireCrates;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

@Converters(LocationTypeConverter.class)
@Indexes(@Index(fields = @Field(value = "name"), options = @IndexOptions(unique = true)))
@Entity(noClassnameStored = true, value = "crates")
public class Crate
{

    @Id
    private int id;

    private String name;

    private String stub;

    private boolean active;

    private boolean firework;

    private boolean broadcast;

    private double knockback;

    private List<String> hologramLines;

    private List<Location> locations;

    @Embedded
    private List<Reward> rewards;

    @Embedded
    private Key key;

    @Transient
    private double totalPercent;

    @Transient
    private List<Hologram> holograms;

    @Transient
    private ItemStack item;

    public Crate()
    {
        hologramLines = new LinkedList<>();
        rewards = new LinkedList<>();
        holograms = new LinkedList<>();
        locations = new LinkedList<>();
    }

    protected void assignDefaults(String name)
    {
        this.name = name;
        this.stub = name.toLowerCase();
        this.active = true;
        this.firework = DesireCrates.getConfigHandler().getBoolean("crates.default.firework");
        this.broadcast = DesireCrates.getConfigHandler().getBoolean("crates.default.broadcast");
        this.knockback = DesireCrates.getConfigHandler().getDouble("crates.default.knockback");
        this.hologramLines = DesireCrates.getConfigHandler().getStringList("crates.default.holograms");
        this.key = new Key().assignDefaults(this);
    }

    protected void loadLocations()
    {
        // iterate over all the crates
        ListIterator<Location> it = getLocations().listIterator();
        while (it.hasNext())
        {
            Location loc = it.next();

            // check and apply the block data
            try
            {
                Block block = loc.getBlock();

                // ensure the block is actually a chest
                if (block != null && block.getType() == Material.CHEST)
                {
                    // if it is, set the metadata
                    block.setMetadata(CrateHandler.META, new CrateMetadata(this));
                }
                else
                {
                    // if it isn't, remove the location
                    it.remove();
                    CrateHandler.saveCrate(this);
                    continue;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // generate the holograms
            try
            {
                Hologram hologram = HologramsAPI.createHologram(DesireCrates.getInstance(), loc.add(0.5, 0.5, 0.5));
                for (String line : hologramLines)
                {
                    hologram.appendTextLine(line);
                }
                holograms.add(hologram);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    protected void loadRewards()
    {
        // calculate the total percent
        for (Reward reward : getRewards())
        {
            totalPercent += reward.getChance();
        }
    }

    protected void unloadHolograms()
    {
        // delete all existing holograms
        for (Hologram holo : holograms)
        {
            holo.delete();
        }
    }

    @IdGetter
    public int getId()
    {
        return id;
    }

    public ItemStack getItem()
    {
        if (item == null)
        {
            item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(getName() + " Crate");
            item.setItemMeta(meta);
        }
        return item.clone();
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        this.stub = name.toLowerCase();
    }

    public String getStub()
    {
        return stub;
    }

    /**
     * @return the firework
     */
    public boolean isFirework()
    {
        return firework;
    }

    /**
     * @param firework the firework to set
     */
    public void setFirework(boolean firework)
    {
        this.firework = firework;
    }

    /**
     * @return the broadcast
     */
    public boolean isBroadcast()
    {
        return broadcast;
    }

    /**
     * @param broadcast the broadcast to set
     */
    public void setBroadcast(boolean broadcast)
    {
        this.broadcast = broadcast;
    }

    /**
     * @return the knockback
     */
    public double getKnockback()
    {
        return knockback;
    }

    /**
     * @param knockback the knockback to set
     */
    public void setKnockback(double knockback)
    {
        this.knockback = knockback;
    }

    /**
     * @return the rewards
     */
    public List<Reward> getRewards()
    {
        return rewards;
    }

    /**
     * @return the key
     */
    public Key getKey()
    {
        return key;
    }

    /**
     * @return the totalPercent
     */
    public double getTotalPercent()
    {
        return totalPercent;
    }

    /**
     * @param totalPercent the totalPercent to set
     */
    public void setTotalPercent(double totalPercent)
    {
        this.totalPercent = totalPercent;
    }

    /**
     * Adds a line to the holograms for the crates. This saves it to the database as well as adds it to currently loaded
     * holograms
     * 
     * @param line the text to add
     */
    public void addHologramLine(String line)
    {
        hologramLines.add(line);
        CrateHandler.saveCrate(this);
        for (Hologram holo : holograms)
        {
            holo.appendTextLine(line);
        }
    }

    /**
     * @return the hologramLines
     */
    public List<String> getHologramLines()
    {
        return hologramLines;
    }

    /**
     * @return the holograms
     */
    public List<Hologram> getHolograms()
    {
        return holograms;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Key key)
    {
        this.key = key;
        CrateHandler.saveCrate(this);
    }

    /**
     * @return the locations
     */
    public List<Location> getLocations()
    {
        return locations;
    }

    /**
     * Add a new location to the Crate. This will also spawn in holograms and any other effects associated with this
     * crate.
     * 
     * @param block the block for the new crate location.
     */
    public void addLocation(Block block)
    {
        locations.add(block.getLocation());
        Hologram hologram = HologramsAPI.createHologram(DesireCrates.getInstance(), block.getLocation().add(0.5, 0.5, 0.5));
        for (String line : hologramLines)
        {
            hologram.appendTextLine(line);
        }
        holograms.add(hologram);
        CrateHandler.saveCrate(this);
    }
}
