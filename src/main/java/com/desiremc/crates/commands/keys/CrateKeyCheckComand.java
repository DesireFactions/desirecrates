package com.desiremc.crates.commands.keys;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyCheckComand extends ValidCommand
{

    public CrateKeyCheckComand()
    {
        super("check", "Check how many crate keys you have.");

        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .setRequiredRank(Rank.MODERATOR)
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = (Crate) args.get(0).getValue();

        if (args.get(1).hasValue())
        {
            int amount = crate.getPendingKeys(sender.getUniqueId());

            if (amount <= 0)
            {
                DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.self.none",
                        "{crate}", crate.getName());
            }
            else
            {
                DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.self.amount",
                        "{crate}", crate.getName(),
                        "{amount}", amount);
            }
        }
        else
        {
            Session target = (Session) args.get(1).getValue();
            int amount = crate.getPendingKeys(target.getUniqueId());
            if (amount <= 0)
            {
                DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.others.none",
                        "{crate}", crate.getName(),
                        "{target}", target.getName());
            }
            else
            {
                DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.others.amount",
                        "{crate}", crate.getName(),
                        "{amount}", amount,
                        "{target}", target.getName());
            }
        }
    }

}
