package com.desiremc.crates.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.api.items.ItemFlag;
import com.desiremc.core.api.items.ItemFlagHandler;
import com.desiremc.crates.DesireCrates;

@Embedded
public class Key
{

    private Material material;

    private String name;

    private List<String> lore;

    private boolean enchanted;

    @Transient
    private Crate crate;

    @Transient
    private ItemStack item;

    public Key()
    {
        lore = new ArrayList<>();
    }

    protected void setCrate(Crate crate)
    {
        this.crate = crate;
    }

    protected Key assignDefaults(Crate crate)
    {
        material = Material.TRIPWIRE_HOOK;
        name = DesireCrates.getConfigHandler().getString("key.defaults.name").replace("{crate}", crate.getName());
        lore = DesireCrates.getConfigHandler().getStringList("key.defaults.lore");
        for (int i = 0; i < lore.size(); i++)
        {
            lore.set(i, lore.get(i).replace("{crate}", crate.getName()));
        }
        enchanted = DesireCrates.getConfigHandler().getBoolean("key.defualts.glow");
        return this;
    }

    public ItemStack getItem()
    {
        if (item == null)
        {
            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            if (enchanted)
            {
                item.addEnchantment(Enchantment.DURABILITY, 5);
                item = ItemFlagHandler.addItemFlags(item, ItemFlag.HIDE_ENCHANTS);
            }
        }
        return item.clone();
    }

    public boolean isKey(ItemStack item)
    {
        return item != null && item.getType() == getItem().getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(name);
    }

    /**
     * @return the material
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(Material material)
    {
        this.material = material;
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
     * @return the enchanted
     */
    public boolean isEnchanted()
    {
        return enchanted;
    }

    /**
     * @param enchanted the enchanted to set
     */
    public void setEnchanted(boolean enchanted)
    {
        this.enchanted = enchanted;
    }

    /**
     * @return the lore
     */
    public List<String> getLore()
    {
        return lore;
    }

    /**
     * @return the crate
     */
    public Crate getCrate()
    {
        return crate;
    }

}
