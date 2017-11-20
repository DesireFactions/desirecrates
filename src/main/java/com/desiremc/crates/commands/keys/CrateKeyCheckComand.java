package com.desiremc.crates.commands.keys;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.SessionUtils;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyCheckComand extends ValidCommand
{

    public CrateKeyCheckComand()
    {
        super("check", "Check how many crate keys you have.", Rank.GUEST, ARITY_OPTIONAL, new String[] { "crate", "target" });

        addParser(new CrateParser(), "crate");
        addParser(new PlayerSessionParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        Crate crate = (Crate) args[0];

        if (args.length == 1)
        {
            int amount = crate.getPendingKeys(p.getUniqueId());

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
        else if (!SessionUtils.getRank(p).isManager())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "no_permissions");
        }
        else
        {
            Session target = (Session) args[1];
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
