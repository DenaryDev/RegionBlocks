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
import io.sapphiremc.regionblocks.region.block.BlockData;
import io.sapphiremc.regionblocks.region.block.BlockDataSerializer;
import io.sapphiremc.regionblocks.region.block.RegionBlock;
import io.sapphiremc.regionblocks.region.block.BrokenBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@SuppressWarnings("ClassCanBeRecord")
public class BlocksListener implements Listener {

    private final RegionBlocksPlugin plugin;

    public BlocksListener(RegionBlocksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getRegionManager().canBreak(event.getPlayer()))
            return;
        Block block = event.getBlock();

        Region region = plugin.getRegionManager().getRegionAtLocation(block.getLocation());
        if (region == null)
            return;

        event.setCancelled(true);
        for (RegionBlock regionBlock : region.getRegionBlocks()) {
            if (regionBlock.compareBlock(block)) {
                event.setCancelled(false);
                Location location = block.getLocation();
                BlockData blockData = BlockDataSerializer.serialize(block);
                BrokenBlock brokenBlock = new BrokenBlock(location, blockData, region.getBlocksAtLocation(location).size());

                if (regionBlock.isUseTempBlock()) {
                    Bukkit.getScheduler().runTask(plugin, () ->
                            BlockDataSerializer.apply(block, regionBlock.getTempBlockData()));
                }

                if (regionBlock.getRegenSeconds() > 0) {
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
