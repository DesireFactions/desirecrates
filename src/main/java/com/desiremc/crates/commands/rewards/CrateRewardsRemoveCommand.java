package com.desiremc.crates.commands.rewards;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.parsers.RewardParser;

public class CrateRewardsRemoveCommand extends ValidCommand
{

    public CrateRewardsRemoveCommand()
    {
        super("remove", "Remove a reward from the crate.", Rank.ADMIN, new String[] { "delete" });

        addArgument(CommandArgumentBuilder.createBuilder(Reward.class)
                .setName("reward")
                .setParser(new RewardParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Reward reward = (Reward) args.get(0).getValue();
        Crate crate = reward.getCrate();

        crate.removeReward(reward);
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.delete",
                "{reward}", reward.getName());
    }

}
