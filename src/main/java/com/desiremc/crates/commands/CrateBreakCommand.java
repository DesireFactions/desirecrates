package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.CrateHandler;

public class CrateBreakCommand extends ValidCommand
{

    public CrateBreakCommand()
    {
        super("break", "Break a crate location.", Rank.ADMIN, new String[] {});

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        DesireCrates.getLangHandler().sendRenderMessage(sender, "breaking." + (CrateHandler.toggleBreaking(p.getUniqueId()) ? "enable" : "disable"));
    }

}
