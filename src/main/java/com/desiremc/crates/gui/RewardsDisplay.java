package com.desiremc.crates.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;

public class RewardsDisplay extends Menu
{

    private static HashMap<UUID, RewardsDisplay> displays = new HashMap<>();

    private Crate crate;

    private EditStatus status;

    public RewardsDisplay(Crate crate, UUID uuid)
    {
        super(crate.getName() + "Crate", ((crate.getRewards().size() + 8) / 9) + 2);

        this.crate = crate;
        displays.put(uuid, this);
    }

    public void populateItems()
    {
        int index = 0;
        for (Reward reward : crate.getRewards())
        {
            addMenuItem(new EditReward(reward), index);
            index++;
        }

        addMenuItem(new MenuItem("§cDelete Item", new MaterialData(Material.WOOL), (short) 14)
        {

            @Override
            public void onClick(Player player)
            {
                status = EditStatus.DELETE;
            }
        }, 3, getRows() - 1);
        addMenuItem(new MenuItem("§7Cancel", new MaterialData(Material.WOOL), (short) 0)
        {

            @Override
            public void onClick(Player player)
            {
                status = null;
            }
        }, 4, getRows() - 1);
        addMenuItem(new MenuItem("§eEdit Item", new MaterialData(Material.WOOL), (short) 4)
        {

            @Override
            public void onClick(Player player)
            {
                status = EditStatus.EDIT;
            }
        }, 5, getRows() - 1);
    }

    private EditStatus getStatus()
    {
        return status;
    }

    private void clearStatus()
    {
        status = null;
    }

    private static EditStatus getStatus(UUID uuid)
    {
        RewardsDisplay display = displays.get(uuid);
        if (display == null)
        {
            return null;
        }
        return display.getStatus();
    }

    private static void clearStatus(UUID uuid)
    {
        RewardsDisplay display = displays.get(uuid);
        if (display == null)
        {
            return;
        }
        display.clearStatus();
    }

    private static enum EditStatus
    {
        DELETE,
        EDIT;
    }

    private static class EditReward extends MenuItem
    {

        private Reward reward;

        public EditReward(Reward reward)
        {
            super(reward.getItem());
            this.reward = reward;
        }

        @Override
        public void onClick(Player player)
        {
            EditStatus status = RewardsDisplay.getStatus(player.getUniqueId());
            if (status == EditStatus.DELETE)
            {
                reward.getCrate().removeReward(reward);
                getMenu().removeMenuItem(getSlot());
                clearStatus(player.getUniqueId());
            }

        }

    }

}
