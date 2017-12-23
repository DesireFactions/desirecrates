package com.desiremc.crates.parsers;

import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
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
public class RewardParser implements Parser<Reward>
{

    @Override
    public Reward parseArgument(Session sender, String[] label, String arg)
    {
        // check that the player is editing a crate
        if (!new EditingCrateValidator().validate(sender))
        {
            return null;
        }

        // get the crate the player is editing
        Crate crate = CrateRewardsEditCommand.getEditing(sender.getUniqueId());

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

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
