package com.desiremc.crates.commands.rewards.add;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.core.validators.NumberSizeValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.validators.EditingCrateValidator;
import com.desiremc.crates.validators.UnusedRewardNameValidator;

public class CrateRewardsAddItemCommand extends ValidCommand
{

    public CrateRewardsAddItemCommand()
    {
        super("item", "Add a reward to the crate.", Rank.ADMIN, true);

        addSenderValidator(new EditingCrateValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new UnusedRewardNameValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Double.class)
                .setName("chance")
                .setParser(new DoubleParser())
                .addValidator(new NumberSizeValidator<>(0.0, 100.0))
                .build());

        addSenderValidator(new ItemInHandValidator());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();
        Crate crate = CrateRewardsEditCommand.getEditing(sender.getUniqueId());

        ItemStack is = player.getItemInHand();
        String name = (String) args.get(0).getValue();
        double chance = (Double) args.get(1).getValue();

        Reward reward = new Reward();

        reward.setChance(chance);
        reward.setItem(is);
        reward.setName(name);
        reward.setType(RewardType.ITEM);

        crate.addReward(reward);
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.add", true, false,
                "{reward}", reward.getName(),
                "{crate}", crate.getName());
    }

}
