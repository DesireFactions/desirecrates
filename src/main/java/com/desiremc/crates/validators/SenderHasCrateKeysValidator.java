package com.desiremc.crates.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;

public class SenderHasCrateKeysValidator implements Validator<Crate>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Crate arg)
    {
        int amount = arg.getPendingKeys(sender.getUniqueId());

        if (amount <= 0)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.self.none", true, false,
                    "{crate}", arg.getName());
            return false;
        }

        return true;
    }

}
