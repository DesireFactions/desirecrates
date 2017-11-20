package com.desiremc.crates.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.gui.PreviewDisplay;
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

    private Map<UUID, Integer> pendingKeys;

    @Embedded
    private LinkedList<Reward> rewards;

    @Embedded
    private Key key;

    @Transient
    private Map<Location, Hologram> holograms;

    @Transient
    private ItemStack item;

    @Transient
    private PreviewDisplay previewDisplay;

    public Crate()
    {
        hologramLines = new LinkedList<>();
        locations = new LinkedList<>();
        pendingKeys = new HashMap<>();
        rewards = new LinkedList<>();
        holograms = new HashMap<>();
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
                Hologram hologram = HologramsAPI.createHologram(DesireCrates.getInstance(), loc.clone().add(0.5, 0.5, 0.5));
                for (String line : hologramLines)
                {
                    hologram.appendTextLine(line);
                }
                holograms.put(loc, hologram);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    protected void unloadHolograms()
    {
        // delete all existing holograms
        for (Hologram holo : holograms.values())
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
     * Adds a line to the holograms for the crates. This saves it to the database as well as adds it to currently loaded
     * holograms
     * 
     * @param line the text to add
     */
    public void addHologramLine(String line)
    {
        hologramLines.add(line);
        CrateHandler.saveCrate(this);
        for (Hologram holo : holograms.values())
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
    public Collection<Hologram> getHolograms()
    {
        return holograms.values();
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
        holograms.put(block.getLocation(), hologram);
        CrateHandler.saveCrate(this);
    }

    /**
     * Remove a location from the Crate. This will also despawn the holograms and any other effects associated with this
     * crate.
     * 
     * @param block the block of the crate to be broken.
     */
    public void removeLocation(Block block)
    {
        locations.remove(block.getLocation());
        Hologram holo = holograms.get(block.getLocation());
        holo.delete();
        CrateHandler.deleteCrate(this);
    }

    /**
     * This will generate the preview display if it had not been previously generated.
     * 
     * @return the preview
     */
    public PreviewDisplay getPreviewDisplay()
    {
        if (previewDisplay == null)
        {
            previewDisplay = new PreviewDisplay(this);
        }
        return previewDisplay;
    }

    public void open(Player player)
    {
        Reward reward = getRandomReward();
        if (firework)
        {
            // TODO spawn firework
        }
        if (broadcast)
        {
            // TODO broadcast message
            Bukkit.broadcastMessage("Yaaaay he won");
        }
        if (reward.getType() == RewardType.COMMAND)
        {
            for (String str : reward.getCommands())
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/" + str);
            }
        }
        else
        {
            player.getInventory().addItem(reward.getItem());
        }
    }

    private Reward getRandomReward()
    {
        double weight = 0;
        for (Reward reward : rewards)
        {
            weight += reward.getChance();
        }
        double value = new Random().nextDouble() * weight;
        for (Reward reward : rewards)
        {
            value -= reward.getChance();
            if (value <= 0)
            {
                return reward;
            }
        }
        return rewards.peekLast();
    }

    public void addPendingKeys(UUID uuid, int amount)
    {
        Integer val = pendingKeys.get(uuid);
        if (val != null)
        {
            pendingKeys.put(uuid, val + amount);
        }
        else
        {
            pendingKeys.put(uuid, amount);
        }
    }
    
    public int getPendingKeys(UUID uuid)
    {
        return pendingKeys.get(uuid);
    }
}
