/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region;

import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import io.sapphiremc.regionblocks.region.block.BrokenBlock;
import io.sapphiremc.regionblocks.region.block.RegionBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Region {

    private final List<RegionBlock> regionBlocks = new ArrayList<>();
    private final List<BrokenBlock> brokenBlocks = new ArrayList<>();
    private final List<String> names;

    @SuppressWarnings("ConstantConditions")
    public Region(List<String> names, ConfigurationSection section) {
        this.names = names;
        Random random = new Random();
        for (String key : section.getKeys(false)) {
            if (section.contains(key) && section.isConfigurationSection(key)) {
                regionBlocks.add(new RegionBlock(random, key, section.getConfigurationSection(key)));
            } else {
                RegionBlocksPlugin.getInstance().getLogger().severe("Blocks section for region " + key + " not found!");
            }
        }
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
        List<BrokenBlock> blocksAtLocation = getBlocksAtLocation(location);
        if (blocksAtLocation.size() > 0) {
            return blocksAtLocation.get(blocksAtLocation.size() - 1);
        } else {
            return null;
        }
    }

    public List<BrokenBlock> getBlocksAtLocation(Location location) {
        return brokenBlocks.stream().filter(brokenBlock -> brokenBlock.getLocation().equals(location)).toList();
    }
}
