/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import io.sapphiremc.regionblocks.region.block.BlockDataSerializer;
import io.sapphiremc.regionblocks.region.block.BrokenBlock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RegionManager {

    private final RegionBlocksPlugin plugin;

    @Getter
    private final List<Region> regions = new ArrayList<>();
    private final List<UUID> toggled = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    public void reloadRegions(FileConfiguration config) {
        regions.clear();

        if (config.contains("regions") && config.isConfigurationSection("regions")) {
            for (String key : config.getConfigurationSection("regions").getKeys(false)) {
                if (config.contains("regions." + key) && config.isConfigurationSection("regions." + key)) {
                    List<String> names = key.contains(";") ? Arrays.stream(key.split(";")).toList() : List.of(key);
                    Region region = new Region(names, config.getConfigurationSection("regions." + key));
                    System.out.println("RegionBlocks size for region " + key + " is " + region.getRegionBlocks().size());
                    if (region.getRegionBlocks().size() > 0) regions.add(region);
                } else {
                    plugin.getLogger().severe("Configuration section for region " + key + " not found!");
                }
            }
        } else {
            plugin.getLogger().severe("Regions section in config not found!");
        }
    }

    public Region getRegionByName(String name) {
        for (Region region : regions) {
            if (region.getNames().contains(name)) return region;
        }
        return null;
    }

    public Region getRegionAtLocation(Location loc) {
        int priority = -1;
        String regionName = "";
        com.sk89q.worldguard.protection.managers.RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        if (regionManager == null) return null;
        ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.adapt(loc).toVector().toBlockPoint());
        for (ProtectedRegion region : set) {
            if (region.getPriority() > priority) {
                priority = region.getPriority();
                regionName = region.getId();
            }
        }
        for (Region regionToReturn : regions) {
            if (regionToReturn.getNames().contains(regionName))
                return regionToReturn;
        }
        return null;
    }

    public void regenAllRegions() {
        for (Region region : regions) {
            regenRegion(region);
        }
    }

    public void regenRegion(Region region) {
        for (BrokenBlock block : region.getBrokenBlocks()) {
            regenBlock(region, block.getLocation());
        }
    }

    public void regenBlock(Region region, Location location) {
        BrokenBlock block = region.getBrokenBlock(location);
        if (block == null) return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            BlockDataSerializer.apply(location.getBlock(), block.getBlockData());
            region.removeBrokenBlock(block);
        });
    }

    public boolean canBreak(Player player) {
        return toggled.contains(player.getUniqueId());
    }

    public void setCanBreak(Player player, boolean canBreak) {
        if (canBreak) {
            toggled.add(player.getUniqueId());
        } else {
            toggled.remove(player.getUniqueId());
        }
    }
}
