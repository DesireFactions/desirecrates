package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.crates.parsers.CrateParser;

public class CrateDeleteCommand extends ValidCommand
{

    public CrateDeleteCommand()
    {
        super("delete", "Delete a crate.", Rank.ADMIN, new String[] { "name" }, new String[] { "remove" });

        addParser(new CrateParser(), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Crate crate = (Crate) args[0];
        
        CrateHandler.deleteCrate(crate);
        
        DesireCrates.getLangHandler().sendRenderMessage(sender, "delete", "{crate}", crate.getName());
    }

}
