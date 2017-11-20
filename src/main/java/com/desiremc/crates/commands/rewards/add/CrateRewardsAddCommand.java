package com.desiremc.crates.commands.rewards.add;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class CrateRewardsAddCommand extends ValidBaseCommand
{

    public CrateRewardsAddCommand()
    {
        super("add", "Add a reward to a crate", Rank.ADMIN);

        addSubCommand(new CrateRewardsAddItemCommand());
        addSubCommand(new CrateRewardsAddCommandCommand());
    }

}
