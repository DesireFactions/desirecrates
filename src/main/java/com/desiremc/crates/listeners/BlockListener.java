package com.desiremc.crates.listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.tickets.TicketHandler;
import com.desiremc.core.utils.SessionUtils;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class BlockListener implements Listener
{

    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        if (CrateHandler.getCrate(e.getItemDrop().getItemStack()) != null)
        {
            e.setCancelled(true);
            DesireCrates.getLangHandler().sendRenderMessage(e.getPlayer(), "no_drop");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent e)
    {
        Iterator<ItemStack> it = e.getDrops().iterator();
        while (it.hasNext())
        {
            ItemStack item = it.next();
            if (CrateHandler.getCrate(item) != null)
            {
                it.remove();
            }
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent e)
    {
        if (e.getSource() != e.getDestination())
        {
            if (CrateHandler.getCrate(e.getItem()) != null)
            {
                e.setCancelled(true);
                if (e.getInitiator().getHolder() instanceof Player)
                {
                    DesireCrates.getLangHandler().sendRenderMessage((Player) e.getInitiator().getHolder(), "no_drop");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (CrateHandler.getCrate(e.getCurrentItem()) != null)
        {
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player)
            {
                DesireCrates.getLangHandler().sendRenderMessage((Player) e.getWhoClicked(), "no_drop");
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Crate crate = CrateHandler.getCrate(e.getItemInHand());

        if (crate != null)
        {
            if (SessionUtils.getRank(e.getPlayer()).isManager())
            {
                crate.addLocation(e.getBlock());
            }
            else
            {
                Player p = e.getPlayer();
                p.setItemInHand(null);
                TicketHandler.openTicket(Bukkit.getConsoleSender(), p.getName() + " was in possession of a crate object.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e)
    {
        if (e.isCancelled())
        {
            return;
        }

        Crate crate = CrateHandler.getCrate(e.getBlock());

        if (crate != null)
        {
            if (!CrateHandler.isBreaking(e.getPlayer().getUniqueId()))
            {
                e.setCancelled(true);
            }
        }
    }

}
