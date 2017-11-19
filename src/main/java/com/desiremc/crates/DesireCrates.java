package com.desiremc.crates;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;

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

    }

    private void registerCommands()
    {

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
    
}
