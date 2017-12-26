package com.desiremc.crates.parsers;

import java.util.List;

import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.CrateHandler;

public class HistoricalCrateParser implements Parser<Crate>
{

    @Override
    public Crate parseArgument(Session sender, String[] label, String arg)
    {
        Crate crate = CrateHandler.getHistoricalCrate(arg);
        if (crate != null)
        {
            return crate;
        }

        DesireCrates.getLangHandler().sendRenderMessage(sender, "not_found", true, false);
        return null;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}
