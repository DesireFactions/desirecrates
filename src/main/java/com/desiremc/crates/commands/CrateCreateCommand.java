package com.desiremc.crates.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.crates.validators.UnusedCrateNameValidator;

public class CrateCreateCommand extends ValidCommand
{

    public CrateCreateCommand()
    {
        super("create", "Create a new crate.", Rank.ADMIN, new String[] { "new" });

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new UnusedCrateNameValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = CrateHandler.createCrate((String) args.get(0).getValue());

        DesireCrates.getLangHandler().sendRenderMessage(sender, "create", "{crate}", crate.getName());
    }

}
