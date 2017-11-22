package com.desiremc.crates.commands.rewards.add;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.parsers.CrateParser;
import com.desiremc.crates.validators.UnusedRewardNameValidator;

public class CrateRewardsAddItemCommand extends ValidCommand
{

    public CrateRewardsAddItemCommand()
    {
        super("item", "Add a reward to the plugin.", Rank.ADMIN, new String[] { "crate", "name", "chance" });

        addParser(new CrateParser(), "crate");
        addParser(new StringParser(), "name");
        addParser(new DoubleParser(), "chance");

        addValidator(new ItemInHandValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Crate crate = (Crate) args[0];

        if (!new UnusedRewardNameValidator(crate).validateArgument(sender, label, args[1]))
        {
            return;
        }

        Player p = (Player) sender;
        ItemStack is = p.getItemInHand();
        String name = (String) args[1];
        double chance = (double) args[2];

        Reward reward = new Reward();

        reward.setChance(chance);
        reward.setItem(is);
        reward.setName(name);
        reward.setType(RewardType.ITEM);

        crate.addReward(reward);
    }

}
