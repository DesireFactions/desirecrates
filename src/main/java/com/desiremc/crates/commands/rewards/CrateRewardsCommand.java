package com.desiremc.crates.commands.rewards;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.commands.rewards.add.CrateRewardsAddCommand;
import com.desiremc.crates.commands.rewards.edit.CrateRewardsEditCommand;

public class CrateRewardsCommand extends ValidBaseCommand
{

    public CrateRewardsCommand()
    {
        super("rewards", "Manage the rewards for a crate.", Rank.ADMIN, "reward");

        addSubCommand(new CrateRewardsAddCommand());
        addSubCommand(new CrateRewardsEditCommand());
    }

}
