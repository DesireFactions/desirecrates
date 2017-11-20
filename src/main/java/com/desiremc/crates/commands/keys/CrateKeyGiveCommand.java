package com.desiremc.crates.commands.keys;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.IntegerSizeValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyGiveCommand extends ValidCommand
{

    public CrateKeyGiveCommand()
    {
        super("give", "Give a key to a player.", Rank.ADMIN, new String[] { "player", "crate", "amount" }, new String[] { "send" });

        addParser(new PlayerSessionParser(), "player");
        addParser(new CrateParser(), "crate");
        addParser(new IntegerParser(), "amount");

        addValidator(new IntegerSizeValidator(0, 10), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session s = (Session) args[0];
        Crate crate = (Crate) args[1];
        int amount = (int) args[2];

        DesireCrates.getLangHandler().sendRenderMessage(s.getPlayer(), "keys.receive",
                "{amount}", amount,
                "{crate}", crate.getName());

        DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.send",
                "{amount}", amount,
                "{crate}", crate.getName(),
                "{player}", s.getName());
        
        crate.addPendingKeys(s.getUniqueId(), amount);
    }

}
