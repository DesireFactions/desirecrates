package com.desiremc.crates.gui;

import org.bukkit.entity.Player;

import com.desiremc.core.gui.Menu;
import com.desiremc.core.gui.MenuItem;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;

public class PreviewDisplay extends Menu
{

    private Crate crate;
    
    public PreviewDisplay(Crate crate)
    {
        super(crate.getName() + "Crate", (crate.getRewards().size() + 8) / 9);
        
        this.crate = crate;
    }
    
    public void populateItems()
    {
        int index = 0;
        for (Reward reward : crate.getRewards())
        {
            addMenuItem(new MenuItem(reward.getItem())
            {
                @Override
                public void onClick(Player player)
                {
                    
                }
            }, index);
            index++;
        }
    }

}
