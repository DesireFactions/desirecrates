package com.desiremc.crates.commands.keys;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.NumberSizeValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyGiveCommand extends ValidCommand
{

    public CrateKeyGiveCommand()
    {
        super("give", "Give a key to a player.", Rank.ADMIN, new String[] { "send" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("player")
                .setParser(new SessionParser())
                .build());
        
        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());
        
        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new IntegerParser())
                .addValidator(new NumberSizeValidator<>(0, 10))
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session s = (Session) args.get(0).getValue();
        Crate crate = (Crate) args.get(1).getValue();
        int amount = (Integer) args.get(2).getValue();

        DesireCrates.getLangHandler().sendRenderMessage(s.getPlayer(), "keys.receive", true, false,
                "{amount}", amount,
                "{crate}", crate.getName());

        DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.send", true, false,
                "{amount}", amount,
                "{crate}", crate.getName(),
                "{player}", s.getName());

        crate.addPendingKeys(s.getUniqueId(), amount);
    }

}
