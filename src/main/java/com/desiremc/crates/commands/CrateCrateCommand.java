package com.desiremc.crates.commands;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newvalidators.SenderHasFreeSlotValidator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateCrateCommand extends ValidCommand
{

    public CrateCrateCommand()
    {
        super("crate", "Get a crate item.", Rank.ADMIN, true);

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

        ItemStack item = crate.getItem();

        sender.getPlayer().getInventory().addItem(item);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "item", "{crate}", crate.getName());
    }

}
