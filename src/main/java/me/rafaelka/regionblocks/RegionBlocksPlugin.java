/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks;

import lombok.Getter;
import me.rafaelka.regionblocks.commands.RegionBlocksCommand;
import me.rafaelka.regionblocks.listeners.BlocksListener;
import me.rafaelka.regionblocks.region.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RegionBlocksPlugin extends JavaPlugin {

    @Getter
    private static RegionBlocksPlugin instance;

    @Getter
    private volatile RegionManager regionManager;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        regionManager = new RegionManager(this);

        Bukkit.getPluginManager().registerEvents(new BlocksListener(this), this);
        getCommand("regionblocks").setExecutor(new RegionBlocksCommand(this));

        onReload();
    }

    public void onReload() {
        reloadConfig();
        regionManager.reloadRegions(getConfig());
    }
}
