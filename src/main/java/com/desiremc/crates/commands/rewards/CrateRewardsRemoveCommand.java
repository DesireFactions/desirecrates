package com.desiremc.crates.commands.rewards;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.parsers.RewardParser;

public class CrateRewardsRemoveCommand extends ValidCommand
{

    public CrateRewardsRemoveCommand()
    {
        super("remove", "Remove a reward from the crate.", Rank.ADMIN, new String[] { "reward" }, new String[] { "delete" });

        addParser(new RewardParser(), "reward");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Reward reward = (Reward) args[0];
        Crate crate = reward.getCrate();

        crate.removeReward(reward);
        crate.save();
        
        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.delete",
                "{reward}", reward.getName());
    }

}
