package com.desiremc.crates.validators;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.utils.PlayerUtils;

public class PlayerHasKeysValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (arg == null)
        {
            UUID uuid = PlayerUtils.getUUIDFromSender(sender);
            
        }
        else
        {

        }
        return false;
    }

}
