package com.desiremc.crates.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.CrateHandler;

public class CrateBreakCommand extends ValidCommand
{

    public CrateBreakCommand()
    {
        super("break", "Break a crate location.", Rank.ADMIN, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireCrates.getLangHandler().sendRenderMessage(sender,
                "breaking." + (CrateHandler.toggleBreaking(sender.getUniqueId()) ? "enable" : "disable"), true, false);
    }

}
