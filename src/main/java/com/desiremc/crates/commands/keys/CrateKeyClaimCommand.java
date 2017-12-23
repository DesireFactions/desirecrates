package com.desiremc.crates.commands.keys;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.SenderHasFreeSlotValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateKeyClaimCommand extends ValidCommand
{

    public CrateKeyClaimCommand()
    {
        super("claim", "Claim pending keys.", true);

        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());

        addSenderValidator(new SenderHasFreeSlotValidator());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = (Crate) args.get(0).getValue();
        int amount = crate.getPendingKeys(sender.getUniqueId());

        ItemStack item = crate.getKey().getItem();
        item.setAmount(amount);

        sender.getPlayer().getInventory().addItem(item);

        crate.clearPendingKeys(sender.getUniqueId());
        crate.save();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "keys.claim",
                "{crate}", crate.getName(),
                "{amount}", amount);
    }

}
