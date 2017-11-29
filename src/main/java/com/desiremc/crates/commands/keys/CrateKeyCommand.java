package com.desiremc.crates.commands.keys;

import com.desiremc.core.api.command.ValidBaseCommand;
import com.desiremc.core.session.Rank;

public class CrateKeyCommand extends ValidBaseCommand
{

    public CrateKeyCommand()
    {
        super("key", "Manage crate keys.", Rank.GUEST, new String[] { "keys" });

        addSubCommand(new CrateKeyGiveCommand());
        addSubCommand(new CrateKeyClaimCommand());
        addSubCommand(new CrateKeyCheckComand());
    }

}
