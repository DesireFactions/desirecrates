package com.desiremc.crates.commands.rewards;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateRewardsEditCommand extends ValidCommand
{

    private static HashMap<UUID, Crate> editing = new HashMap<>();

    public CrateRewardsEditCommand()
    {
        super("edit", "Edit the rewards.", Rank.ADMIN, new String[] { "crate" });

        addParser(new CrateParser(), "crate");

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        Crate crate = (Crate) args[0];

        setEditing(player, crate);
        
        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.editing",
                "{crate}", crate.getName());
    }

    /**
     * Get which crate the player is currently editing. Can easily be used to check if a player is editing a crate as
     * well.
     * 
     * @param player the player.
     * @return the crate the player is editing.
     */
    public static Crate getEditing(Player player)
    {
        return editing.get(player.getUniqueId());
    }

    /**
     * Sets which crate the player is currently editing. If crate is null, it will remove the player from the map if
     * they are in it.
     * 
     * @param player the player.
     * @param crate the crate.
     */
    public static void setEditing(Player player, Crate crate)
    {
        if (crate == null)
        {
            editing.remove(player.getUniqueId());
        }
        else
        {
            editing.put(player.getUniqueId(), crate);
        }
    }

}
