package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.DesireCrates;

public class CrateReloadCommand extends ValidCommand
{

    public CrateReloadCommand()
    {
        super("reload", "Reload the config and lang files.", Rank.DEVELOPER, new String[0]);
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        DesireCrates.loadConfig();
        DesireCrates.loadLang();
        sender.sendMessage("Reloaded crates config and lang.");
    }

}
