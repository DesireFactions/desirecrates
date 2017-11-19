package com.desiremc.crates.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Reward
{

    private String name;

    private double chance;

    private RewardType type;

    private ItemStack item;

    private List<String> commands;

    public Reward()
    {
        commands = new ArrayList<>();
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the chance
     */
    public double getChance()
    {
        return chance;
    }

    /**
     * @param chance the chance to set
     */
    public void setChance(double chance)
    {
        this.chance = chance;
    }

    /**
     * @return the type
     */
    public RewardType getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(RewardType type)
    {
        this.type = type;
    }

    /**
     * @return the item
     */
    public ItemStack getItem()
    {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    /**
     * @return the commands
     */
    public List<String> getCommands()
    {
        return commands;
    }

    /**
     * @param commands the commands to set
     */
    public void setCommands(List<String> commands)
    {
        this.commands = commands;
    }

    public static enum RewardType
    {
        ITEM,
        COMMAND;
    }

}
