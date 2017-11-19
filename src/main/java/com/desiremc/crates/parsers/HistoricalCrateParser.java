package com.desiremc.crates.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class HistoricalCrateParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        Crate crate = CrateHandler.getHistoricalCrate(arg);
        if (crate != null)
        {
            return crate;
        }

        DesireCrates.getLangHandler().sendRenderMessage(sender, "not_found");
        return null;
    }

}
