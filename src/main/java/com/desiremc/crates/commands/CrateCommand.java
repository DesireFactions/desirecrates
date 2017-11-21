package com.desiremc.crates.commands;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.commands.keys.CrateKeyCommand;
import com.desiremc.crates.commands.rewards.CrateRewardsCommand;

public class CrateCommand extends ValidBaseCommand
{

    public CrateCommand()
    {
        super("crate", "View and manage crates.", Rank.ADMIN, new String[] { "crates" });

        addSubCommand(new CrateCreateCommand());
        addSubCommand(new CrateCrateCommand());
        addSubCommand(new CrateKeyCommand());
        addSubCommand(new CrateRewardsCommand());
        addSubCommand(new CrateListCommand());
        addSubCommand(new CrateBreakCommand());
        addSubCommand(new CrateDeleteCommand());
        addSubCommand(new CrateRestoreCommand());
        addSubCommand(new CrateReloadCommand());
    }

}
