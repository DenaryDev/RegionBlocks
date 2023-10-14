/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rafaelka.regionblocks.RegionBlocksPlugin;
import me.rafaelka.regionblocks.region.block.BlockDataSerializer;
import me.rafaelka.regionblocks.region.block.BrokenBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class RegionManager {

    private final RegionBlocksPlugin plugin;

    @Getter
    private final List<Region> regions = new ArrayList<>();
    private final List<UUID> toggled = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    public void reloadRegions(FileConfiguration config) {
        for (final var region : regions) {
            regenRegion(region);
        }
        regions.clear();

        if (config.contains("regions") && config.isConfigurationSection("regions")) {
            for (final var key : config.getConfigurationSection("regions").getKeys(false)) {
                if (config.contains("regions." + key) && config.isConfigurationSection("regions." + key)) {
                    final var names = key.contains(";") ? Arrays.stream(key.split(";")).toList() : List.of(key);
                    final var region = new Region(names, config.getConfigurationSection("regions." + key));
                    if (!region.getRegionBlocks().isEmpty() || region.hasBerries()) regions.add(region);
                } else {
                    plugin.getLogger().severe("Configuration section for region " + key + " not found!");
                }
            }
        } else {
            plugin.getLogger().severe("Regions section in config not found!");
        }
    }

    public Region getRegionByName(String name) {
        for (final var region : regions) {
            if (region.getNames().contains(name)) return region;
        }
        return null;
    }

    public List<Region> getRenewableRegionsAt(Location loc) {
        final var wgRegions = getWGRegionsAt(loc);
        if (wgRegions == null) return Collections.emptyList();
        final var wgRegionNames = wgRegions.getRegions().stream().map(ProtectedRegion::getId).toList();
        if (regions.isEmpty()) return Collections.emptyList();

        final var result = new ArrayList<Region>();
        for (final var region : regions) {
            if (region.getNames().stream().anyMatch(wgRegionNames::contains)) {
                result.add(region);
            }
        }
        return result;
    }

    private ApplicableRegionSet getWGRegionsAt(Location loc) {
        final var regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld()));
        if (regionManager == null) return null;
        return regionManager.getApplicableRegions(BukkitAdapter.adapt(loc).toVector().toBlockPoint());
    }

    public void regenRegions(List<Region> regions) {
        for (Region region : regions) {
            regenRegion(region);
        }
    }

    public void regenRegion(Region region) {
        for (BrokenBlock block : region.getBrokenBlocks()) {
            regenBlock(region, block.location(), true);
        }
    }

    public void regenBlock(Region region, Location location, boolean withCommand) {
        final var block = region.getBrokenBlock(location);
        if (block == null) return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            BlockDataSerializer.apply(location.getBlock(), block.blockData());
            region.removeBrokenBlock(block);
        });

        if ((!withCommand || plugin.getConfig().getBoolean("use-particles-with-command", false)) && block.useRegenParticle()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                    location.getWorld().spawnParticle(block.regenParticleType(), location.toCenterLocation().add(0, 0.6, 0), block.regenParticleCount(), .175, .05, .175, block.regenParticleExtra())
            );
        }
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
