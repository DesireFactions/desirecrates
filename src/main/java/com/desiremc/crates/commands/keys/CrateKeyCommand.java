package com.desiremc.crates.commands.keys;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class CrateKeyCommand extends ValidBaseCommand
{

    public CrateKeyCommand()
    {
        super("key", "Manage crate keys.", new String[] { "keys" });

        addSubCommand(new CrateKeyGiveCommand());
        addSubCommand(new CrateKeyClaimCommand());
        addSubCommand(new CrateKeyCheckComand());
    }

}
