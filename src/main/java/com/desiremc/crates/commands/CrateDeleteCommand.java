package com.desiremc.crates.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.crates.parsers.CrateParser;

public class CrateDeleteCommand extends ValidCommand
{

    public CrateDeleteCommand()
    {
        super("delete", "Delete a crate.", Rank.ADMIN, new String[] { "remove" });

        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());

    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = (Crate) args.get(0).getValue();

        CrateHandler.deleteCrate(crate);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "delete", true, false, "{crate}", crate.getName());
    }

}
