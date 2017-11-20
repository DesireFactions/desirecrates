package com.desiremc.crates.commands.keys;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.validators.PlayerHasKeysValidator;

public class CrateKeyClaimCommand extends ValidCommand
{

    public CrateKeyClaimCommand()
    {
        super("claim", "Claim pending keys.", Rank.GUEST, new String[] {});
        
        addValidator(new PlayerValidator());
        addValidator(new PlayerHasKeysValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        
    }

}
