package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.crates.parsers.HistoricalCrateParser;

public class CrateRestoreCommand extends ValidCommand
{

    public CrateRestoreCommand()
    {
        super("restore", "Restore a historical crate.", Rank.ADMIN, new String[] { "crate" }, new String[] { "undelete" });

        addParser(new HistoricalCrateParser(), "crate");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Crate crate = (Crate) args[0];
        CrateHandler.restoreCrate(crate);
        
        DesireCrates.getLangHandler().sendRenderMessage(sender, "restore", "{crate}", crate.getName());
    }

}
