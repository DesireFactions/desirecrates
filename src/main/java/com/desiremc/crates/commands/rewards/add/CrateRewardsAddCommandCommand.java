package com.desiremc.crates.commands.rewards.add;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.DoubleParser;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.NumberSizeValidator;
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
        super("command", "Add a command reward. Held item used as the display.", Rank.ADMIN, true);

        addSenderValidator(new EditingCrateValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("name")
                .setParser(new StringParser())
                .addValidator(new UnusedRewardNameValidator())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Double.class)
                .setName("chance")
                .setParser(new DoubleParser())
                .addValidator(new NumberSizeValidator<Double>(0.0, 100.0))
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("command")
                .setParser(new StringParser())
                .setVariableLength()
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = CrateRewardsEditCommand.getEditing(sender.getUniqueId());

        String name = (String) args.get(0).getValue();
        double chance = (Double) args.get(1).getValue();
        String command = (String) args.get(2).getValue();

        Reward reward = new Reward();
        reward.setChance(chance);
        reward.setName(name);
        reward.setType(RewardType.COMMAND);

        ItemStack is;
        if (sender.getPlayer().getItemInHand() != null && sender.getPlayer().getItemInHand().getType() != Material.AIR)
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

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.add", true, false,
                "{reward}", reward.getName(),
                "{crate}", crate.getName());
    }

}
