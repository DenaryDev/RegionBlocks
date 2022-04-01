/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.listeners;

import io.sapphiremc.regionblocks.region.Region;
import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import io.sapphiremc.regionblocks.region.RegionBlock;
import io.sapphiremc.regionblocks.region.BrokenBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlocksListener implements Listener {

    private final RegionBlocksPlugin plugin;

    public BlocksListener(RegionBlocksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    @SuppressWarnings("deprecation")
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getRegionManager().canBreak(event.getPlayer()))
            return;
        Block block = event.getBlock();

        Region region = plugin.getRegionManager().getRegionAtLocation(block.getLocation());
        if (region == null)
            return;

        event.setCancelled(true);
        //if (plugin.isShudownInit()) return;
        for (RegionBlock regionBlock : region.getRegionBlocks()) {
            if (regionBlock.compareBlock(block)) {
                event.setCancelled(false);
                Location location = block.getLocation();
                BrokenBlock brokenBlock = new BrokenBlock(location, block.getType(), block.getData(), region.getBlocksAtLocation(location).size());

                if (regionBlock.isUseTemporaryMaterial())
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        block.setType(regionBlock.getTemporaryMaterial());
                        block.setData(regionBlock.getTemporaryData(), true);
                    });
                if (regionBlock.getRegenSeconds() != -1) {
                    region.addBrokenBlock(brokenBlock);
                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            plugin.getRegionManager().regenBlock(region, block.getLocation()),
                            regionBlock.getRegenSeconds() * 20L);
                }
                return;
            }
        }
    }
}
