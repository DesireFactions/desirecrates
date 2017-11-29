package com.desiremc.crates.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.utils.ItemUtils;

@Embedded
public class Reward
{

    private String name;

    private double chance;

    private RewardType type;

    private String item;

    private List<String> commands;

    @Transient
    private ItemStack parsedItem;

    @Transient
    private Crate crate;

    public Reward()
    {
        commands = new ArrayList<>();
    }

    protected void setCrate(Crate crate)
    {
        this.crate = crate;
    }

    public Crate getCrate()
    {
        return crate;
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
        if (parsedItem == null)
        {
            parsedItem = ItemUtils.deserializeItem(item);
        }
        return parsedItem;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemStack parsedItem)
    {
        this.parsedItem = CraftItemStack.asCraftCopy(parsedItem);
        this.item = ItemUtils.serializeItem(this.parsedItem);
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
