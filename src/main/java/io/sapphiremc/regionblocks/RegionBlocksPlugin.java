/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks;

import io.sapphiremc.regionblocks.commands.RegionBlocksCommand;
import io.sapphiremc.regionblocks.listeners.BlocksListener;
import io.sapphiremc.regionblocks.region.RegionManager;
import lombok.Getter;
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
