package com.desiremc.crates.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.utils.Utils;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.gui.PreviewDisplay;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;

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

    private List<String> locations;

    private Map<UUID, Integer> pendingKeys;

    @Embedded
    private LinkedList<Reward> rewards;

    @Embedded
    private Key key;

    @Transient
    private Map<Location, List<Hologram>> holograms;

    @Transient
    private ItemStack item;

    @Transient
    private List<Location> parsedLocations;

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
        for (int i = 0; i < hologramLines.size(); i++)
        {
            hologramLines.set(i, hologramLines.get(i).replace("{crate}", getName()));
        }
        this.key = new Key().assignDefaults(this);
    }

    protected void loadLocations()
    {
        // iterate over all the crates
        for (Location loc : getLocations())
        {
            boolean exit = false;

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
                    Bukkit.getScheduler().runTask(DesireCrates.getInstance(), () -> removeLocation(block));
                    exit = true;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if (!exit)
            {
                // generate the holograms
                try
                {
                    Location clone = loc.clone();
                    clone.add(0.5, 1.75, 0.5);
                    Iterator<String> lines = hologramLines.iterator();
                    String line;
                    Hologram holo = null;
                    List<Hologram> holos = new LinkedList<>();
                    while (lines.hasNext())
                    {
                        line = lines.next();
                        if (holo == null)
                        {
                            holo = HologramAPI.createHologram(clone, line);
                            holo.spawn();
                        }
                        else
                        {
                            holo = holo.addLineBelow(line);
                        }
                        holos.add(holo);
                    }
                    holograms.put(loc, holos);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    protected void unloadHolograms()
    {
        // delete all existing holograms
        for (List<Hologram> holos : holograms.values())
        {
            for (Hologram holo : holos)
            {
                holo.despawn();
            }
        }
    }

    @IdGetter
    public int getId()
    {
        return id;
    }

    /**
     * @return the visual representation of this item.
     */
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

    /**
     * @return if the crate is active.
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * @param active whether or not the chest is active.
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     * @return the name of the chest.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the new name of the crate
     */
    public void setName(String name)
    {
        this.name = name;
        this.stub = name.toLowerCase();
        CrateHandler.saveCrate(this);
    }

    /**
     * Gets the stub of the crate. Usually this is the lowercase version of the name, however it does not necessarily
     * have to be.
     * 
     * @return the stub of the crate.
     */
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
        CrateHandler.saveCrate(this);
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
        CrateHandler.saveCrate(this);
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

    public Reward getReward(String name)
    {
        for (Reward reward : getRewards())
        {
            if (reward.getName().equalsIgnoreCase(name))
            {
                return reward;
            }
        }
        return null;
    }

    /**
     * @param reward the new reward to add.
     */
    public void addReward(Reward reward)
    {
        this.rewards.add(reward);
        CrateHandler.saveCrate(this);
    }

    /**
     * @param reward the new reward to add.
     */
    public void removeReward(Reward reward)
    {
        this.rewards.remove(reward);
        CrateHandler.saveCrate(this);
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
        for (List<Hologram> holos : holograms.values())
        {
            ListIterator<Hologram> it = holos.listIterator();
            Hologram holo = null;
            while (it.hasNext())
                holo = it.next();
            it.add(holo.addLineBelow(line));
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
        List<Hologram> holos = new LinkedList<>();
        holograms.values().forEach(x -> holos.addAll(holos));
        return holos;
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
        if (parsedLocations == null)
        {
            parsedLocations = new LinkedList<>();
            for (String str : locations)
            {
                parsedLocations.add(Utils.toLocation(str));
            }
        }
        return parsedLocations;
    }

    /**
     * Add a new location to the Crate. This will also spawn in holograms and any other effects associated with this
     * crate.
     * 
     * @param block the block for the new crate location.
     */
    public void addLocation(Block block)
    {
        getLocations().add(block.getLocation());
        locations.add(Utils.toString(block.getLocation()));
        Location clone = block.getLocation().clone().add(0.5, 1.75, 0.5);
        Iterator<String> lines = hologramLines.iterator();
        String line;
        Hologram holo = null;
        List<Hologram> holos = new LinkedList<>();
        while (lines.hasNext())
        {
            line = lines.next();
            if (holo == null)
            {
                holo = HologramAPI.createHologram(clone, line);
                holo.spawn();
            }
            else
            {
                holo = holo.addLineBelow(line);
            }
            holos.add(holo);
        }
        holograms.put(block.getLocation(), holos);
        block.setMetadata(CrateHandler.META, new CrateMetadata(this));
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
        getLocations().remove(block.getLocation());
        locations.remove(Utils.toString(block.getLocation()));
        List<Hologram> holo = holograms.get(block.getLocation());
        if (holo != null)
        {
            holo.forEach(x ->
            {
                if (x.isSpawned())
                {
                    x.despawn();
                }
            });
        }
        block.removeMetadata(CrateHandler.META, DesireCrates.getInstance());
        CrateHandler.saveCrate(this);
    }

    /**
     * This will generate the preview display if it had not been previously generated.
     * 
     * @return the preview
     */
    public PreviewDisplay getPreviewDisplay()
    {

        PreviewDisplay previewDisplay = new PreviewDisplay(this);
        previewDisplay.populateItems();
        return previewDisplay;
    }

    /**
     * Run the entire process of this crate being opened by the specified player.
     * 
     * @param player the player opening the crate.
     */
    public void open(Player player)
    {
        Reward reward = getRandomReward();
        if (reward == null)
        {
            return;
        }
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
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
            }
        }
        else
        {
            player.getInventory().addItem(reward.getItem());
            player.updateInventory();
        }
        ItemStack item = player.getItemInHand();
        if (item != null)
        {
            if (item.getAmount() == 1)
            {
                player.setItemInHand(null);
            }
            else
            {
                item.setAmount(item.getAmount() - 1);
                player.setItemInHand(item);
            }
        }
    }

    /**
     * Goes through all the rewards and randomly picks one based on the given percentages.
     * 
     * @return the random reward.
     */
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

    /**
     * Give keys to a player to be claimed whenever the player wants to. This allows for players to get keys when they
     * are offline without them losing it, as well as ensuring we don't have to make sure they have an empty slot in
     * their inventory.
     * 
     * @param uuid the player to give the keys to.
     * @param amount the amount of keys.
     */
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

    /**
     * Gets how many keys a player has. If the player has none, it returns null.
     * 
     * @param uuid the player to check.
     * @return the amount of pending keys the player has.
     */
    public int getPendingKeys(UUID uuid)
    {
        return pendingKeys.get(uuid);
    }
}
