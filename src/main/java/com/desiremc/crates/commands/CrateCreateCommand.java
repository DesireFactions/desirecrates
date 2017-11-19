package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.crates.validators.UnusedCrateNameValidator;

public class CrateCreateCommand extends ValidCommand
{

    public CrateCreateCommand()
    {
        super("create", "Create a new crate.", Rank.ADMIN, new String[] { "name" }, new String[] { "new" });

        addParser(new StringParser(), "name");

        addValidator(new UnusedCrateNameValidator(), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Crate crate = CrateHandler.createCrate((String) args[0]);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "create", "{crate}", crate.getName());
    }

}
