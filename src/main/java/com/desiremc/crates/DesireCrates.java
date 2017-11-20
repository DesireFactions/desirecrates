package com.desiremc.crates;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.crates.commands.CrateCommand;
import com.desiremc.crates.listeners.BlockListener;
import com.desiremc.crates.listeners.ConnectionListener;
import com.desiremc.crates.listeners.InteractListener;

public class DesireCrates extends JavaPlugin
{

    private static DesireCrates instance;

    private static FileHandler configHandler;
    private static LangHandler langHandler;

    public void onEnable()
    {
        instance = this;

        loadConfig();
        loadLang();

        registerListeners();
        registerCommands();
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new BlockListener(), this);
        listenerManager.addListener(new ConnectionListener(), this);
        listenerManager.addListener(new InteractListener(), this);
    }

    private void registerCommands()
    {
        CustomCommandHandler commandHandler = CustomCommandHandler.getInstance();
        commandHandler.registerCommand(new CrateCommand(), this);
    }

    public static void loadConfig()
    {
        File file = new File(instance.getDataFolder(), "config.yml");
        instance.saveDefaultConfig();
        configHandler = new FileHandler(file, instance);
    }

    public static void loadLang()
    {
        File file = new File(instance.getDataFolder(), "lang.yml");
        if (!file.exists())
        {
            instance.saveResource("lang.yml", false);
        }
        langHandler = new LangHandler(file, instance);
    }

    public static LangHandler getLangHandler()
    {
        return langHandler;
    }

    public static FileHandler getConfigHandler()
    {
        return configHandler;
    }

    public static DesireCrates getInstance()
    {
        return instance;
    }

}
