package com.desiremc.crates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateCrateCommand extends ValidCommand
{

    public CrateCrateCommand()
    {
        super("crate", "Get a crate item.", Rank.ADMIN, new String[] { "crate" });

        addParser(new CrateParser(), "crate");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;
        Crate crate = (Crate) args[0];

        ItemStack item = crate.getItem();

        p.getInventory().addItem(item);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "item", "{crate}", crate.getName());
    }

}
