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
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.data.Reward.RewardType;
import com.desiremc.crates.validators.EditingCrateValidator;
import com.desiremc.crates.validators.UnusedRewardNameValidator;

public class CrateRewardsAddCommandCommand extends ValidCommand
{

    public CrateRewardsAddCommandCommand()
    {
        super("command", "Add a command reward. Held item used as the display.", Rank.ADMIN, ARITY_REQUIRED_VARIADIC, new String[] { "name", "chance", "command" });

        addParser(new StringParser(), "name");
        addParser(new DoubleParser(), "chance");
        addParser(new StringParser(), "command");

        addValidator(new PlayerValidator());
        addValidator(new EditingCrateValidator());
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

        String name = (String) args[0];
        double chance = (double) args[1];
        String command = (String) args[2];

        Reward reward = new Reward();
        reward.setChance(chance);
        reward.setName(name);
        reward.setType(RewardType.COMMAND);

        ItemStack is;
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR)
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
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.add",
                "{reward}", reward.getName(),
                "{crate}", crate.getName());
    }

}
