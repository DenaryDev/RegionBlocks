/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.listeners;

import lombok.RequiredArgsConstructor;
import me.rafaelka.regionblocks.RegionBlocksPlugin;
import me.rafaelka.regionblocks.region.Region;
import me.rafaelka.regionblocks.region.block.BlockData;
import me.rafaelka.regionblocks.region.block.BlockDataSerializer;
import me.rafaelka.regionblocks.region.block.BrokenBlock;
import me.rafaelka.regionblocks.region.block.RegionBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class BlocksListener implements Listener {

    private final RegionBlocksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getRegionManager().canBreak(event.getPlayer()))
            return;
        final var block = event.getBlock();

        final var regions = plugin.getRegionManager().getRenewableRegionsAt(block.getLocation());
        if (regions.isEmpty()) return;
        event.setCancelled(true);

        final var player = event.getPlayer();

        checkRegions:
        for (final var region : regions) {
            if (region.checkBreak(player)) {
                for (final var regionBlock : region.getRegionBlocks()) {
                    if (regionBlock.compareBlock(block)) {
                        event.setCancelled(false);
                        processBlockBreak(region, block, regionBlock);
                        break checkRegions;
                    }
                }
            }
        }
    }

    private void processBlockBreak(Region region, Block block, RegionBlock regionBlock) {
        final var location = block.getLocation();
        final var blockData = new BlockData(block.getType(), block.getBlockData()); //BlockDataSerializer.serialize(block);
        final var brokenBlock = new BrokenBlock(location, blockData, region.getBlocksAtLocation(location).size(), regionBlock.isUseRegenParticle(), regionBlock.getRegenParticleType(), regionBlock.getRegenParticleCount(), regionBlock.getRegenParticleExtra());

        if (regionBlock.isUseTempBlock()) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    BlockDataSerializer.apply(block, regionBlock.getTempBlockData()));
        }

        if (regionBlock.getRegenSeconds() > 0) {
            region.addBrokenBlock(brokenBlock);
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                            plugin.getRegionManager().regenBlock(region, block.getLocation(), false),
                    regionBlock.getRegenSeconds() * 20L);
        }
    }
}
