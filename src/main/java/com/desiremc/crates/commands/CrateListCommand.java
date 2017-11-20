package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class CrateListCommand extends ValidCommand
{

    public CrateListCommand()
    {
        super("list", "List off the existing crates.", Rank.GUEST, new String[] {}, new String[] { "show" });
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "list.header");
        for (Crate crate : CrateHandler.getCrates())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "list.text",
                    "{crate}", crate.getName());
        }

    }

}
