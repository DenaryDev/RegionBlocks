/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

@Getter
@SuppressWarnings("deprecation")
public class RegionBlock {
    private final int regenSeconds;
    private final boolean useTemporaryMaterial;
    private final boolean checkBlockData;
    private Material material;
    private byte data = 0;
    private Material temporaryMaterial;
    private byte temporaryData;

    public RegionBlock(String material, ConfigurationSection section) {
        if (material.contains(":")) {
            data = Byte.parseByte(material.split(":")[1]);
            material = material.split(":")[0];
        }
        try {
            this.material = Material.getMaterial(Integer.parseInt(material));
        } catch (Exception ex) {
            this.material = Material.getMaterial(material);
        }

        regenSeconds = section.getInt("regen-time", -1);
        checkBlockData = section.getBoolean("check-block-data", false);
        useTemporaryMaterial = section.getBoolean("use-temp-block", false);

        if (useTemporaryMaterial) {
            material = section.getString("temp-block", "BEDROCK");
            if (material.contains(":")) {
                temporaryData = Byte.parseByte(material.split(":")[1]);
                material = material.split(":")[0];
            }
            try {
                this.temporaryMaterial = Material.getMaterial(Integer.parseInt(material));
            } catch (Exception ex) {
                this.temporaryMaterial = Material.getMaterial(material);
            }
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
