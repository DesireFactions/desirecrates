package com.desiremc.crates.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;

public class UnusedRewardNameValidator extends CommandValidator
{

    private Crate crate;

    public UnusedRewardNameValidator(Crate crate)
    {
        this.crate = crate;
    }

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        String name = (String) arg;

        if (crate.getReward(name) != null)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "reward.already_exists");
            return false;
        }

        return true;
    }

}
