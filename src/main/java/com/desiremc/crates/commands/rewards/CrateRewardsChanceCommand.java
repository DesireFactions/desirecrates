package com.desiremc.crates.commands.rewards;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.DoubleSizeValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.parsers.RewardParser;

public class CrateRewardsChanceCommand extends ValidCommand
{

    public CrateRewardsChanceCommand()
    {
        super("chance", "Change the chance of a reward", Rank.ADMIN, new String[] { "reward", "chance" }, new String[] { "percent" });

        addParser(new RewardParser(), "reward");
        addParser(new DoubleParser(), "chance");

        addValidator(new DoubleSizeValidator(0, 100), "chance");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Reward reward = (Reward) args[0];
        Double chance = (Double) args[1];
        Crate crate = reward.getCrate();

        reward.setChance(chance);
        crate.save();
        
        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.chance",
                "{reward}", reward.getName(),
                "{crate}", crate.getName(),
                "{chance}", reward.getChance());
    }

}
