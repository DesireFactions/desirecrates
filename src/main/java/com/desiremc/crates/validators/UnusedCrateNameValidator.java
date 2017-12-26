package com.desiremc.crates.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.CrateHandler;

public class UnusedCrateNameValidator implements Validator<String>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        if (CrateHandler.getCrate(arg) == null && CrateHandler.getHistoricalCrate(arg) == null)
        {
            return true;
        }

        DesireCrates.getLangHandler().sendRenderMessage(sender, "used_name", true, false);
        return false;
    }

}
