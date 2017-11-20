package com.desiremc.crates.commands.keys;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.FreeSlotValidator;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyClaimCommand extends ValidCommand
{

    public CrateKeyClaimCommand()
    {
        super("claim", "Claim pending keys.", Rank.GUEST, new String[] { "crate" });

        addParser(new CrateParser(), "crate");

        addValidator(new PlayerValidator());
        addValidator(new FreeSlotValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        Crate crate = (Crate) args[0];
        int amount = crate.getPendingKeys(p.getUniqueId());

        if (amount <= 0)
        {
            DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.check.none",
                    "{crate}", crate.getName());
            return;
        }

        ItemStack item = crate.getKey().getItem();
        item.setAmount(amount);

        p.getInventory().addItem(item);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.claim",
                "{crate}", crate.getName(),
                "{amount}", amount);
    }

}
