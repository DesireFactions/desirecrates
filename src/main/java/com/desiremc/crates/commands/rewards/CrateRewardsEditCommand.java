package com.desiremc.crates.commands.rewards;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.parsers.CrateParser;

public class CrateRewardsEditCommand extends ValidCommand
{

    private static HashMap<UUID, Crate> editing = new HashMap<>();

    public CrateRewardsEditCommand()
    {
        super("edit", "Edit the rewards.", Rank.ADMIN, true);

        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());

    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = (Crate) args.get(0).getValue();

        setEditing(sender, crate);

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.editing", true, false,
                "{crate}", crate.getName());
    }

    /**
     * Get which crate the player is currently editing. Can easily be used to check if a player is editing a crate as
     * well.
     * 
     * @param player the player.
     * @return the crate the player is editing.
     */
    public static Crate getEditing(UUID uuid)
    {
        return editing.get(uuid);
    }

    /**
     * Sets which crate the player is currently editing. If crate is null, it will remove the player from the map if
     * they are in it.
     * 
     * @param player the player.
     * @param crate the crate.
     */
    public static void setEditing(Session player, Crate crate)
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
