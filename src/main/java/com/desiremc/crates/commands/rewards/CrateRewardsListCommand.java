package com.desiremc.crates.commands.rewards;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.crates.DesireCrates;
import com.desiremc.crates.data.Crate;
import com.desiremc.crates.data.Reward;
import com.desiremc.crates.parsers.CrateParser;

public class CrateRewardsListCommand extends ValidCommand
{

    public CrateRewardsListCommand()
    {
        super("list", "List all rewards.", Rank.ADMIN, true);

        addArgument(CommandArgumentBuilder.createBuilder(Crate.class)
                .setName("crate")
                .setParser(new CrateParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Crate crate = (Crate) args.get(0).getValue();

        DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.list_header", false, false);

        for (Reward reward : crate.getRewards())
        {
            String name = StringUtils.capitalize(reward.getItem().getType().name().replace("_", " ").toLowerCase());
            DesireCrates.getLangHandler().sendRenderMessage(sender, "rewards.list_item", false, false, "{name}", reward.getName(), "{item}", name);
        }
    }
}
