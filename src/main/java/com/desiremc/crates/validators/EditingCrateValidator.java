package com.desiremc.crates.validators;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.commands.rewards.CrateRewardsEditCommand;
import com.desiremc.crates.data.Crate;

/**
 * Used to check if the sender is currently editing a crate.
 * 
 * @author Michael Ziluck
 */
public class EditingCrateValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        Crate crate = CrateRewardsEditCommand.getEditing(sender.getUniqueId());
        if (crate == null)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.not_editing");
            return false;
        }
        return true;
    }

}
