package com.desiremc.crates.commands;

import java.util.Collection;
import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class CrateListCommand extends ValidCommand
{

    public CrateListCommand()
    {
        super("list", "List off the existing crates.", new String[] { "show" });
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Collection<Crate> crates = CrateHandler.getCrates();
        if (crates.size() == 0)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "list.empty");
        }
        else
        {
            DesireCrates.getLangHandler().sendRenderMessageNoPrefix(sender, "list.header");
            for (Crate crate : CrateHandler.getCrates())
            {
                DesireCrates.getLangHandler().sendRenderMessageNoPrefix(sender, "list.text",
                        "{crate}", crate.getName());
            }
        }

    }

}
