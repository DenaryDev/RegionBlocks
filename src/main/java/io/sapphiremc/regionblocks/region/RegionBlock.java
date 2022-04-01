/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region;

import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Random;

@Getter
@SuppressWarnings("deprecation")
public class RegionBlock {
    private final Random random;
    private final boolean useTemporaryMaterial;
    private final boolean checkBlockData;
    private Material material;
    private byte data = 0;
    private int[] regenSeconds;
    private Material temporaryMaterial;
    private byte temporaryData;

    public RegionBlock(Random random, String material, ConfigurationSection section) {
        this.random = random;
        this.checkBlockData = section.getBoolean("check-block-data", false);
        this.useTemporaryMaterial = section.getBoolean("use-temp-block", false);

        if (material.contains(":")) {
            this.data = Byte.parseByte(material.split(":")[1]);
            material = material.split(":")[0];
        }
        try {
            this.material = Material.getMaterial(Integer.parseInt(material));
        } catch (Exception ex) {
            this.material = Material.getMaterial(material);
        }

        if (section.isInt("regen-time")) {
            this.regenSeconds = new int[] { section.getInt("regen-time", -1) };
        } else {
            String[] strings = section.getString("regen-time").split("-");
            if (strings.length == 2) {
                try {
                    int min = Integer.parseInt(strings[0]);
                    int max = Integer.parseInt(strings[1]);
                    this.regenSeconds = new int[] { min, max };
                } catch (IllegalArgumentException ex) {
                    RegionBlocksPlugin.getInstance().getLogger().warning("Invalid regeneration time for block " + getMaterial());
                    ex.printStackTrace();
                }
            }
        }

        if (useTemporaryMaterial) {
            material = section.getString("temp-block", "BEDROCK");
            if (material.contains(":")) {
                this.temporaryData = Byte.parseByte(material.split(":")[1]);
                material = material.split(":")[0];
            }
            try {
                this.temporaryMaterial = Material.getMaterial(Integer.parseInt(material));
            } catch (Exception ex) {
                this.temporaryMaterial = Material.getMaterial(material);
            }
        }
    }

    public int getRegenSeconds() {
        if (regenSeconds.length == 1) {
            return regenSeconds[0];
        } else if (regenSeconds.length == 2) {
            return random.nextInt(regenSeconds[0], regenSeconds[1]);
        } else {
            RegionBlocksPlugin.getInstance().getLogger().warning("Invalid regeneration time for block " + getMaterial() + ", use -1");
            return -1;
        }
    }

    public boolean compareBlock(Block getBlock) {
        if (checkBlockData) {
            return (getBlock.getType().equals(material) && getBlock.getData() == data) || (getBlock.getType().equals(material) && data == -1);
        } else {
            return getBlock.getType().equals(material);
        }
    }
}
