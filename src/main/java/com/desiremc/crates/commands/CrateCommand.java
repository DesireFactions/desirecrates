package com.desiremc.crates.commands;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class CrateCommand extends ValidBaseCommand
{

    public CrateCommand()
    {
        super("crate", "View and manage crates.", Rank.ADMIN, new String[] { "crates" });
        
        addSubCommand(new CrateCreateCommand());
    }

}
