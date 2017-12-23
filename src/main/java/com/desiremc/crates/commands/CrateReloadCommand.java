package com.desiremc.crates.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;

public class CrateReloadCommand extends ValidCommand
{

    public CrateReloadCommand()
    {
        super("reload", "Reload the config and lang files.", Rank.DEVELOPER);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        DesireCrates.loadConfig();
        DesireCrates.loadLang();
        sender.sendMessage("Reloaded crates config and lang.");
    }

}
