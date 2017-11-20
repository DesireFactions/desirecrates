package com.desiremc.crates.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.crates.data.CrateHandler;

public class ConnectionListener implements Listener
{

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        CrateHandler.stopBreaking(e.getPlayer().getUniqueId());
    }
    
}
