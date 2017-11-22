package com.desiremc.crates.commands.rewards.add;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.parsers.CrateParser;
import com.desiremc.crates.validators.UnusedRewardNameValidator;

public class CrateRewardsAddCommandCommand extends ValidCommand
{

    public CrateRewardsAddCommandCommand()
    {
        super("command", "Add a command reward. Held item used as the display.", Rank.ADMIN, ARITY_REQUIRED_VARIADIC, new String[] { "crate", "name", "chance", "command" });

        addParser(new CrateParser(), "crate");
        addParser(new StringParser(), "name");
        addParser(new DoubleParser(), "chance");
        addParser(new StringParser(), "command");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Crate crate = (Crate) args[0];
        if (!new UnusedRewardNameValidator(crate).validateArgument(sender, label, args[1]))
        {
            return;
        }

        String name = (String) args[1];
        double chance = (double) args[2];
        String command = (String) args[3];

        Reward reward = new Reward();
        reward.setChance(chance);
        reward.setName(name);
        reward.setType(RewardType.COMMAND);

        ItemStack is;
        if (sender instanceof Player && ((Player) sender).getItemInHand() != null && ((Player) sender).getItemInHand().getType() != Material.AIR)
        {
            is = ((Player) sender).getItemInHand();
        }
        else
        {
            is = new ItemStack(Material.PAPER);
            ItemMeta meta = is.getItemMeta();
            meta.setDisplayName("§6§lCommand: §e" + name);
            is.setItemMeta(meta);
        }
        reward.setItem(is);
        reward.getCommands().add(command);

        crate.addReward(reward);
    }

}
