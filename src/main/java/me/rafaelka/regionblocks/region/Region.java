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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Getter
public class Region {

    private final List<RegionBlock> regionBlocks = new ArrayList<>();
    private final List<BrokenBlock> brokenBlocks = new ArrayList<>();
    private final List<String> names;
    private final Random random;
    private String permission = null;
    private String permissionMessage = null;
    private int permissionMessageCooldown = 0;

    private final Map<UUID, Integer> messageCooldowns = new HashMap<>();

    private int[] sweetBerriesRegenTime = new int[0];
    private int[] glowBerriesRegenTime = new int[0];

    @SuppressWarnings("ConstantConditions")
    public Region(List<String> names, ConfigurationSection section) {
        this.names = names;
        this.random = new Random();
        if (section.contains("permission") && section.isString("permission")) {
            permission = section.getString("permission");
        }
        if (section.contains("permission-message") && section.isString("permission-message")) {
            permissionMessage = section.getString("permission-message");
        }
        if (section.contains("permission-message-cooldown") && section.isInt("permission-message-cooldown")) {
            permissionMessageCooldown = section.getInt("permission-message-cooldown");
        }

        if (section.contains("blocks") && section.isConfigurationSection("blocks")) {
            final var blocks = section.getConfigurationSection("blocks");
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

        if (section.contains("berries") && section.isConfigurationSection("berries")) {
            final var berries = section.getConfigurationSection("berries");
            if (berries.isInt("sweet-berries-regen-time")) {
                this.sweetBerriesRegenTime = new int[]{berries.getInt("sweet-berries-regen-time", -1)};
            } else if (berries.isString("sweet-berries-regen-time")) {
                final var sweetBerries = berries.getString("sweet-berries-regen-time", "-1").split("-");
                if (sweetBerries.length == 2) {
                    try {
                        final int min = Integer.parseInt(sweetBerries[0]);
                        final int max = Integer.parseInt(sweetBerries[1]);
                        if (min > 0 && max > 0)
                            this.sweetBerriesRegenTime = new int[]{min, max};
                    } catch (IllegalArgumentException ex) {
                        RegionBlocksPlugin.getInstance().getLogger().severe("Invalid regeneration time for sweet berries!");
                        ex.printStackTrace();
                    }
                }
            }
            if (berries.isInt("glow-berries-regen-time")) {
                this.glowBerriesRegenTime = new int[]{berries.getInt("glow-berries-regen-time", -1)};
            } else if (berries.isString("glow-berries-regen-time")) {
                final var glowBerries = berries.getString("glow-berries-regen-time", "-1").split("-");
                if (glowBerries.length == 2) {
                    try {
                        final int min = Integer.parseInt(glowBerries[0]);
                        final int max = Integer.parseInt(glowBerries[1]);
                        if (min > 0 && max > 0)
                            this.glowBerriesRegenTime = new int[]{min, max};
                    } catch (IllegalArgumentException ex) {
                        RegionBlocksPlugin.getInstance().getLogger().severe("Invalid regeneration time for glow berries!");
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (regionBlocks.isEmpty() && sweetBerriesRegenTime.length < 1 && glowBerriesRegenTime.length < 1) {
            RegionBlocksPlugin.getInstance().getLogger().warning("Region " + section.getName() + " doesn't contains any blocks or berries");
        }
    }

    public boolean checkBreak(Player player) {
        final var uuid = player.getUniqueId();
        if (permission != null && !permission.isEmpty() && !player.hasPermission(permission)) {
            if (permissionMessage != null && messageCooldowns.get(uuid) == 0) {
                if (permissionMessageCooldown > 0) {
                    messageCooldowns.put(uuid, permissionMessageCooldown);
                    Bukkit.getScheduler().runTaskLater(RegionBlocksPlugin.getInstance(), () -> messageCooldowns.remove(uuid), permissionMessageCooldown);
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

    public boolean hasBerries() {
        return (sweetBerriesRegenTime.length > 0) || glowBerriesRegenTime.length > 0;
    }

    public int getSweetBerriesRegenTime() {
        if (sweetBerriesRegenTime.length == 1) {
            return sweetBerriesRegenTime[0];
        } else if (sweetBerriesRegenTime.length == 2) {
            return random.nextInt(sweetBerriesRegenTime[0], sweetBerriesRegenTime[1]);
        } else {
            return -2;
        }
    }

    public int getGlowBerriesRegenTime() {
        if (glowBerriesRegenTime.length == 1) {
            return glowBerriesRegenTime[0];
        } else if (glowBerriesRegenTime.length == 2) {
            return random.nextInt(glowBerriesRegenTime[0], glowBerriesRegenTime[1]);
        } else {
            return -2;
        }
    }
}
