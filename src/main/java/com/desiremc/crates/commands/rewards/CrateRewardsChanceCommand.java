package com.desiremc.crates.commands.rewards;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PositiveDoubleParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.NumberSizeValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.parsers.RewardParser;
import com.desiremc.crates.validators.EditingCrateValidator;

public class CrateRewardsChanceCommand extends ValidCommand
{

    public CrateRewardsChanceCommand()
    {
        super("chance", "Change the chance of a reward", Rank.ADMIN, new String[] { "percent" });

        addSenderValidator(new EditingCrateValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Reward.class)
                .setName("reward")
                .setParser(new RewardParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Double.class)
                .setName("chance")
                .setParser(new PositiveDoubleParser())
                .addValidator(new NumberSizeValidator<Double>(0.0, 100.0))
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Reward reward = (Reward) args.get(0).getValue();
        double chance = (Double) args.get(1).getValue();
        Crate crate = reward.getCrate();

        reward.setChance(chance);
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.chance", true, false,
                "{reward}", reward.getName(),
                "{crate}", crate.getName(),
                "{chance}", reward.getChance());
    }

}
