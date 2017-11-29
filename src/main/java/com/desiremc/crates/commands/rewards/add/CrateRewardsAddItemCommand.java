package com.desiremc.crates.commands.rewards.add;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.core.validators.PlayerValidator;
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
        super("item", "Add a reward to the plugin.", Rank.ADMIN, new String[] { "name", "chance" });

        addParser(new StringParser(), "name");
        addParser(new DoubleParser(), "chance");

        addValidator(new PlayerValidator());
        addValidator(new EditingCrateValidator());
        addValidator(new ItemInHandValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Crate crate = CrateRewardsEditCommand.getEditing(player);

        if (!new UnusedRewardNameValidator(crate).validateArgument(sender, label, args[0]))
        {
            return;
        }

        ItemStack is = player.getItemInHand();
        String name = (String) args[0];
        double chance = (double) args[1];

        Reward reward = new Reward();

        reward.setChance(chance);
        reward.setItem(is);
        reward.setName(name);
        reward.setType(RewardType.ITEM);

        crate.addReward(reward);
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.add",
                "{reward}", reward.getName(),
                "{crate}", crate.getName());
    }

}
