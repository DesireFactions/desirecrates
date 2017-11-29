package com.desiremc.crates.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;

public class EditingCrateValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Player player = (Player) sender;

        Crate crate = CrateRewardsEditCommand.getEditing(player);
        if (crate == null)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.not_editing");
            return false;
        }
        return true;
    }

}
