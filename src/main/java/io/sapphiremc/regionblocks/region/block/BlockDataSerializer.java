/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region.block;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Bamboo;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockDataSerializer {
    private static final Pattern TAGS_REPLACER = Pattern.compile("[a-zA-Z]+\\[([a-zA-Z0-9=])+]");
    private static final Pattern TAGS_GETTER = Pattern.compile("");

    public static BlockData serialize(Block block) {
        BlockData blockData = new BlockData(block.getType());

        if (block.getBlockData() instanceof Ageable ageable) {
            blockData.setAge(ageable.getAge());
            blockData.setMaxAge(ageable.getMaximumAge());
        }

        return blockData;
    }

    public static BlockData serialize(String s) {
        System.out.println("Input is " + s);
        Material material = Material.matchMaterial(s.replaceAll(TAGS_REPLACER.pattern(), ""));
        if (material == null) return null;

        BlockData blockData = new BlockData(material);

        String[] tags = s.replace(material.name().toLowerCase(), "").replace("[", "").replace("]", "").split(",");
        System.out.println("Tags is " + Arrays.toString(tags));

        for (String tag : tags) {
            String value = tag.split("=")[1];
            switch (tag) {
                case "age" -> {
                    try {
                        blockData.setAge(Integer.parseInt(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "power" -> {
                    try {
                        blockData.setPower(Integer.parseInt(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "attached" -> {
                    try {
                        blockData.setAttached(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "half" -> {
                    try {
                        blockData.setHalf(Bisected.Half.valueOf());
                    }
                }
                case "facing" -> {

                }
                case "face" -> {

                }
                case "level" -> {
                    try {
                        blockData.setLevel(Integer.parseInt(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "lit" -> {
                    try {
                        blockData.setLit(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "open" -> {
                    try {
                        blockData.setOpen(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "axis" -> {
                    try {
                        blockData.setAxes(Arrays.stream(Axis.values()).collect(Collectors.toSet()));
                        blockData.setAxis(Axis.valueOf(value.toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "powered" -> {
                    try {
                        blockData.setPowered(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "shape" -> {

                }
                case "rotation" -> {

                }
                case "snowy" -> {
                    try {
                        blockData.setSnowy(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
                case "waterlogged" -> {
                    try {
                        blockData.setWaterlogged(Boolean.parseBoolean(value));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }

                case "leaves" -> {
                    try {
                        blockData.setBambooLeaves(Bamboo.Leaves.valueOf(value.toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
