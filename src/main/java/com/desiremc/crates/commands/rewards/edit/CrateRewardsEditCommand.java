package com.desiremc.crates.commands.rewards.edit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.gui.RewardsDisplay;
import com.desiremc.crates.parsers.CrateParser;

public class CrateRewardsEditCommand extends ValidCommand
{

    public CrateRewardsEditCommand()
    {
        super("edit", "Edit the rewards.", Rank.ADMIN, new String[] { "crate" });

        addParser(new CrateParser(), "crate");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;

        RewardsDisplay rewards = new RewardsDisplay((Crate) args[0], player.getUniqueId());
        rewards.populateItems();
        rewards.openMenu(player);
    }

}
