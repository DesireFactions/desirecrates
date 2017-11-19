package com.desiremc.crates.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.CrateHandler;

public class UnusedCrateNameValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (CrateHandler.getCrate((String) arg) == null && CrateHandler.getHistoricalCrate((String) arg) == null)
        {
            return true;
        }

        DesireCrates.getLangHandler().sendRenderMessage(sender, "used_name");
        return false;
    }

}
