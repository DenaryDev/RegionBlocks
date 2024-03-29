/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region.block;

import lombok.Getter;
import me.rafaelka.regionblocks.RegionBlocksPlugin;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RegionBlock {
    private final Random random;

    @Getter
    private final BlockData blockData;
    @Getter
    private boolean useTempBlock = false;
    @Getter
    private BlockData tempBlockData;

    private int[] regenSeconds;

    @Getter
    private boolean useRegenParticle = false;
    @Getter
    private Particle regenParticleType;
    @Getter
    private int regenParticleCount;
    @Getter
    private double regenParticleExtra;

    public RegionBlock(@NotNull Random random, @NotNull ConfigurationSection section) {
        String material = section.getName();
        this.random = random;
        this.blockData = BlockDataSerializer.serialize(material);
        if (this.blockData == null) RegionBlocksPlugin.getInstance().getLogger().severe("Invalid material (" + material + ") for block section '" + section.getName() + "'!");

        if (section.contains("regen-time")) {
            if (section.isInt("regen-time")) {
                this.regenSeconds = new int[]{section.getInt("regen-time", -2)};
            } else if (section.isString("regen-time")){
                String[] strings = section.getString("regen-time", "-2").split("-");
                if (strings.length == 2) {
                    try {
                        int min = Integer.parseInt(strings[0]);
                        int max = Integer.parseInt(strings[1]);
                        this.regenSeconds = new int[]{min, max};
                    } catch (IllegalArgumentException ex) {
                        RegionBlocksPlugin.getInstance().getLogger().severe("Invalid regeneration time for block section '" + section.getName() + "'!");
                        ex.printStackTrace();
                    }
                }
            }
        } else {
            RegionBlocksPlugin.getInstance().getLogger().severe("Invalid regeneration time for block section '" + section.getName() + "'!");
        }

        if (section.contains("regen-particle") && section.isConfigurationSection("regen-particle")) {
            ConfigurationSection regenParticle = section.getConfigurationSection("regen-particle");
            if (regenParticle.contains("type") && regenParticle.isString("type")) {
                try {
                    this.regenParticleType = Particle.valueOf(regenParticle.getString("type", "").toUpperCase());
                    this.regenParticleCount = regenParticle.getInt("count", 3);
                    this.regenParticleExtra = regenParticle.getDouble("extra", 1);
                    useRegenParticle = true;
                } catch (IllegalArgumentException ex) {
                    RegionBlocksPlugin.getInstance().getLogger().warning("Invalid regeneration particle type for block section '" + section.getName() + "'!");
                    ex.printStackTrace();
                }
            } else {
                RegionBlocksPlugin.getInstance().getLogger().warning("Invalid regeneration particle section for block section '" + section.getName() + "'!");
            }
        }

        if (section.contains("temp-block") && section.isString("temp-block")) {
            String tempMaterial = section.getString("temp-block", "");
            if (tempMaterial.isEmpty()) return;
            this.useTempBlock = true;
            this.tempBlockData = BlockDataSerializer.serialize(tempMaterial);
            if (this.tempBlockData == null) RegionBlocksPlugin.getInstance().getLogger().severe("Invalid temporary material (" + tempMaterial + ") for block section '" + section.getName() + "'!");
        }
    }

    public int getRegenSeconds() {
        if (regenSeconds.length == 1) {
            return regenSeconds[0];
        } else if (regenSeconds.length == 2) {
            return random.nextInt(regenSeconds[0], regenSeconds[1]);
        } else {
            return -2;
        }
    }

    public boolean compareBlock(Block block) {
        if (blockData.isHasTags()) {
            if (block.getBlockData() instanceof Ageable ageable) {
                if (ageable.getAge() != blockData.getAge()) return false;
            }
            if (block.getBlockData() instanceof AnaloguePowerable analoguePowerable) {
                if (analoguePowerable.getPower() != blockData.getPower()) return false;
            }
            if (block.getBlockData() instanceof Attachable attachable) {
                if (attachable.isAttached() != blockData.isAttached()) return false;
            }
            if (block.getBlockData() instanceof Bisected bisected) {
                if (bisected.getHalf() != blockData.getHalf()) return false;
            }
            if (block.getBlockData() instanceof Directional directional) {
                if (directional.getFacing() != blockData.getFacing()) return false;
            }
            if (block.getBlockData() instanceof Levelled levelled) {
                if (levelled.getLevel() != blockData.getLevel()) return false;
            }
            if (block.getBlockData() instanceof Lightable lightable) {
                if (lightable.isLit() != blockData.isLit()) return false;
            }
            if (block.getBlockData() instanceof Openable openable) {
                if (openable.isOpen() != blockData.isOpen()) return false;
            }
            if (block.getBlockData() instanceof Orientable orientable) {
                if (orientable.getAxis() != blockData.getAxis()) return false;
            }
            if (block.getBlockData() instanceof Powerable powerable) {
                if (powerable.isPowered() != blockData.isPowered()) return false;
            }
            if (block.getBlockData() instanceof Rail rail) {
                if (rail.getShape() != blockData.getShape()) return false;
            }
            if (block.getBlockData() instanceof Rotatable rotatable) {
                if (rotatable.getRotation() != blockData.getRotation()) return false;
            }
            if (block.getBlockData() instanceof Snowable snowable) {
                if (snowable.isSnowy() != blockData.isSnowy()) return false;
            }
            if (block.getBlockData() instanceof Waterlogged waterlogged) {
                if (waterlogged.isWaterlogged() != blockData.isWaterlogged()) return false;
            }
            if (block.getBlockData() instanceof Bamboo bamboo) {
                if (bamboo.getLeaves() != blockData.getBambooLeaves()) return false;
            }
        }

        return block.getType().equals(blockData.getMaterial());
    }
}
