package com.desiremc.crates.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class InteractListener implements Listener
{

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST)
        {
            Crate crate = CrateHandler.getCrate(e.getClickedBlock());
            if (crate != null)
            {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
                {
                    if (crate.getKey().isKey(e.getItem()))
                    {
                        crate.open(e.getPlayer());
                    }
                    else if (crate.getKnockback() > 0)
                    {
                        e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(-crate.getKnockback()));
                        DesireCrates.getLangHandler().getString("need_key");
                    }
                }
                else if (e.getAction() == Action.LEFT_CLICK_BLOCK)
                {
                    if (CrateHandler.isBreaking(e.getPlayer().getUniqueId()))
                    {
                        crate.removeLocation(e.getClickedBlock());
                        e.getClickedBlock().setType(Material.AIR);
                        CrateHandler.toggleBreaking(e.getPlayer().getUniqueId());
                        DesireCrates.getLangHandler().sendRenderMessage(e.getPlayer(), "breaking.break");
                    }
                    else
                    {
                        crate.getPreviewDisplay().openMenu(e.getPlayer());
                    }
                }
            }
        }

    }

}
