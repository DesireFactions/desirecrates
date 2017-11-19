package com.desiremc.crates.data;

import java.util.LinkedList;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Transient;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

@Entity(noClassnameStored = true, value = "crates")
public class Crate
{

    @Id
    private int id;

    private String name;

    private boolean active;
    
    private boolean firework;

    private boolean broadcast;

    private double knockback;
    
    private List<String> hologramLines;
    
    @Embedded
    private List<Reward> rewards;
    
    @Embedded
    private Key key;

    @Transient
    private double totalPercent;
    
    @Transient
    private List<Hologram> holograms;
    
    public Crate()
    {
        hologramLines = new LinkedList<>();
        rewards = new LinkedList<>();
        holograms = new LinkedList<>();
    }
    
    @IdGetter
    public int getId()
    {
        return id;
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
    }
}
