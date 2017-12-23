package com.desiremc.crates.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;

/**
 * This validator assumes the sender is editing a crate. If they are not, it will fail gracefully but will not send an
 * error message.
 * 
 * @author Michael Ziluck
 */
public class UnusedRewardNameValidator implements Validator<String>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        Crate crate = CrateRewardsEditCommand.getEditing(sender.getUniqueId());
        if (crate == null)
        {
            return false;
        }
        if (crate.getReward(arg) != null)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.already_exists");
            return false;
        }

        return true;
    }

}
