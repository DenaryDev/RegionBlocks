/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region.block;

import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Random;

@Getter
public class RegionBlock {
    private final Random random;
    //private final boolean checkBlockTags;
    private final Material material;
    private boolean useTemporaryMaterial = false;
    private Material temporaryMaterial;
    private int[] regenSeconds;

    public RegionBlock(Random random, String material, ConfigurationSection section) {
        this.random = random;
        //this.checkBlockTags = section.getBoolean("check-block-tags", false);
        this.material = Material.matchMaterial(material);

        if (section.contains("temp-material")) {
            material = section.getString("temp-material", "BEDROCK");
            this.useTemporaryMaterial = true;
            this.temporaryMaterial = Material.matchMaterial(material);
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
        //if (checkBlockTags) {
        //    return (getBlock.getType().equals(material) && getBlock.getData() == data) || (getBlock.getType().equals(material) && data == -1);
        //} else {
            return getBlock.getType().equals(material);
        //}
    }
}
