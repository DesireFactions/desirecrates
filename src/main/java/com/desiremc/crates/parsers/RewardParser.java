package com.desiremc.crates.parsers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.validators.EditingCrateValidator;

/**
 * This parser also behaves like a {@link PlayerValidator}, as well as an {@link EditingCrateValidator}. Both checks are
 * already performed and do not need to be ran again.
 * 
 * @author Michael Ziluck
 */
public class RewardParser implements ArgumentParser
{

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg)
    {
        // check if the sender is a player
        if (!new PlayerValidator().validateArgument(sender, label, null))
        {
            return null;
        }

        // check that the player is editing a crate
        if (!new EditingCrateValidator().validateArgument(sender, label, null))
        {
            return null;
        }

        // get the crate the player is editing
        Crate crate = CrateRewardsEditCommand.getEditing((Player) sender);

        // get the reward by the given name
        Reward reward = crate.getReward(arg);

        // check that the reward exists
        if (reward == null)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.not_found");
            return null;
        }

        // return the reward
        return reward;
    }

}
