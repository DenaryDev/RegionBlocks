/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region;

import lombok.Getter;
import me.rafaelka.regionblocks.RegionBlocksPlugin;
import me.rafaelka.regionblocks.region.block.BrokenBlock;
import me.rafaelka.regionblocks.region.block.RegionBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Region {

    private final List<RegionBlock> regionBlocks = new ArrayList<>();
    private final List<BrokenBlock> brokenBlocks = new ArrayList<>();
    private final List<String> names;
    private String permission = null;
    private String permissionMessage = null;
    private int permissionMessageCooldown = 0;

    @SuppressWarnings("ConstantConditions")
    public Region(List<String> names, ConfigurationSection section) {
        this.names = names;
        if (section.contains("permission") && section.isString("permission")) {
            permission = section.getString("permission");
        }
        if (section.contains("permission-message") && section.isString("permission-message")) {
            permissionMessage = section.getString("permission-message");
        }
        if (section.contains("permission-message-cooldown") && section.isInt("permission-message-cooldown")) {
            permissionMessageCooldown = section.getInt("permission-message-cooldown");
        }

        final var random = new Random();
        if (section.contains("blocks") && section.isConfigurationSection("blocks")) {
            ConfigurationSection blocks = section.getConfigurationSection("blocks");
            for (final var key : blocks.getKeys(false)) {
                if (blocks.contains(key) && blocks.isConfigurationSection(key)) {
                    final var regionblock = new RegionBlock(random, blocks.getConfigurationSection(key));
                    if (regionblock.getBlockData() != null && regionblock.getRegenSeconds() >= -1) {
                        if (regionblock.isUseTempBlock()) {
                            if (regionblock.getTempBlockData() != null) {
                                regionBlocks.add(regionblock);
                            }
                        } else {
                            regionBlocks.add(regionblock);
                        }
                    }
                } else {
                    RegionBlocksPlugin.getInstance().getLogger().severe("Section for block " + key + " in region " + section.getName() + " not found!");
                }
            }
        }
    }

    private int cooldown = 0;
    public boolean checkBreak(Player player) {
        if (permission != null && !permission.isEmpty() && !player.hasPermission(permission)) {
            if (permissionMessage != null && cooldown == 0) {
                if (permissionMessageCooldown > 0) {
                    cooldown = permissionMessageCooldown;
                    Bukkit.getScheduler().runTaskLater(RegionBlocksPlugin.getInstance(), () -> cooldown = 0, permissionMessageCooldown);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage));
            }
            return false;
        }
        return true;
    }

    public void addBrokenBlock(BrokenBlock block) {
        if (!brokenBlocks.contains(block)) {
            brokenBlocks.add(block);
        }
    }

    public void removeBrokenBlock(BrokenBlock block) {
        brokenBlocks.removeIf(brokenBlock -> brokenBlock.equals(block));
    }

    public BrokenBlock getBrokenBlock(Location location) {
        final var blocksAtLocation = getBlocksAtLocation(location);
        if (!blocksAtLocation.isEmpty()) {
            return blocksAtLocation.get(blocksAtLocation.size() - 1);
        } else {
            return null;
        }
    }

    public List<BrokenBlock> getBlocksAtLocation(Location location) {
        return brokenBlocks.stream().filter(brokenBlock -> brokenBlock.location().equals(location)).toList();
    }
}
