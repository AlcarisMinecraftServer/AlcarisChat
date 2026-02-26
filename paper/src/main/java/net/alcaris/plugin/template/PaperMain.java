package net.alcaris.plugin.template;

import org.bukkit.plugin.java.JavaPlugin;

public final class PaperMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Paper plugin enabled!");
        SharedUtil.printStartupMessage("Paper");
    }

    @Override
    public void onDisable() {
        getLogger().info("Paper plugin disabled!");
    }
}