/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region.block;

import me.rafaelka.regionblocks.RegionBlocksPlugin;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.PinkPetals;

import java.util.Arrays;

public class BlockDataSerializer {

    /*public static BlockData serialize(Block block) {
        BlockData blockData = new BlockData(block.getType());

        if (block.getBlockData() instanceof Ageable ageable) {
            blockData.setAge(ageable.getAge());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof AnaloguePowerable analoguePowerable) {
            blockData.setPower(analoguePowerable.getPower());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Attachable attachable) {
            blockData.setAttached(attachable.isAttached());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Bisected bisected) {
            blockData.setHalf(bisected.getHalf());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Brushable brushable) {
            blockData.setDusted(brushable.getDusted());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Directional directional) {
            blockData.setFacing(directional.getFacing());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof FaceAttachable faceAttachable) {
            blockData.setAttachedFace(faceAttachable.getAttachedFace());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Hangable hangable) {
            blockData.setHanging(hangable.isHanging());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Hatchable hatchable) {
            blockData.setHatch(hatchable.getHatch());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Levelled levelled) {
            blockData.setLevel(levelled.getLevel());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Lightable lightable) {
            blockData.setLit(lightable.isLit());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Openable openable) {
            blockData.setOpen(openable.isOpen());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Orientable orientable) {
            blockData.setAxis(orientable.getAxis());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Powerable powerable) {
            blockData.setPowered(powerable.isPowered());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Rail rail) {
            blockData.setShape(rail.getShape());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Rotatable rotatable) {
            blockData.setRotation(rotatable.getRotation());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Snowable snowable) {
            blockData.setSnowy(snowable.isSnowy());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Waterlogged waterlogged) {
            blockData.setWaterlogged(waterlogged.isWaterlogged());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof Bamboo bamboo) {
            blockData.setBambooLeaves(bamboo.getLeaves());
            blockData.setHasTags(true);
        }
        if (block.getBlockData() instanceof PinkPetals pinkPetals) {
            blockData.setFlowerAmount(pinkPetals.getFlowerAmount());
            blockData.setHasTags(true);
        }

        return blockData;
    }*/

    public static BlockData serialize(String s) {
        String mat = s.replaceAll("\\[([a-zA-Z0-9=,])+]", "");
        Material material = Material.matchMaterial(mat);
        if (material == null) {
            RegionBlocksPlugin.getInstance().getLogger().severe("Unable to match material '" + mat + "'!");
            return null;
        } else if (!material.isBlock()) {
            RegionBlocksPlugin.getInstance().getLogger().severe("Material " + mat + " is not a block!");
            return null;
        }

        BlockData blockData = new BlockData(material);

        String[] tags = s.replace(material.name().toLowerCase(), "").replace("[", "").replace("]", "").split(",");

        if (tags.length > 0 && !Arrays.toString(tags).equals("[]")) {
            blockData.setHasTags(true);
            for (String fullTag : tags) {
                String[] strings = fullTag.split("=");
                String tag = strings[0];
                String value = strings[1];
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
                            blockData.setHalf(Bisected.Half.valueOf(value.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "dusted" -> {
                        try {
                            blockData.setDusted(Integer.parseInt(value));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "facing" -> {
                        try {
                            blockData.setFacing(BlockFace.valueOf(value.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "face" -> {
                        try {
                            blockData.setAttachedFace(FaceAttachable.AttachedFace.valueOf(value.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "hanging" -> {
                        try {
                            blockData.setHanging(Boolean.parseBoolean(value));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "hatch" -> {
                        try {
                            blockData.setHatch(Integer.parseInt(value));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
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
                        try {
                            blockData.setShape(Rail.Shape.valueOf(value.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case "rotation" -> {
                        try {
                            blockData.setRotation(BlockFace.valueOf(value.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
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
                    case "flower_amount" -> {
                        try {
                            blockData.setFlowerAmount(Integer.parseInt(value));
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return blockData;
    }

    public static void apply(Block block, BlockData blockData) {
        block.setType(blockData.getMaterial());

        if (blockData.getOriginData() != null) {
            block.setBlockData(blockData.getOriginData());
            return;
        }

        if (block.getBlockData() instanceof Ageable ageable) {
            ageable.setAge(blockData.getAge());
            block.setBlockData(ageable);
        }
        if (block.getBlockData() instanceof AnaloguePowerable analoguePowerable) {
            analoguePowerable.setPower(blockData.getPower());
            block.setBlockData(analoguePowerable);
        }
        if (block.getBlockData() instanceof Attachable attachable) {
            attachable.setAttached(blockData.isAttached());
            block.setBlockData(attachable);
        }
        if (block.getBlockData() instanceof Bisected bisected) {
            bisected.setHalf(blockData.getHalf());
            block.setBlockData(bisected);
        }
        if (block.getBlockData() instanceof Brushable brushable) {
            brushable.setDusted(blockData.getDusted());
            block.setBlockData(brushable);
        }
        if (block.getBlockData() instanceof Directional directional) {
            directional.setFacing(blockData.getFacing());
            block.setBlockData(directional);
        }
        if (block.getBlockData() instanceof FaceAttachable faceAttachable) {
            faceAttachable.setAttachedFace(blockData.getAttachedFace());
            block.setBlockData(faceAttachable);
        }
        if (block.getBlockData() instanceof Hangable hangable) {
            hangable.setHanging(blockData.isHanging());
            block.setBlockData(hangable);
        }
        if (block.getBlockData() instanceof Hatchable hatchable) {
            hatchable.setHatch(blockData.getHatch());
            block.setBlockData(hatchable);
        }
        if (block.getBlockData() instanceof Levelled levelled) {
            levelled.setLevel(blockData.getLevel());
            block.setBlockData(levelled);
        }
        if (block.getBlockData() instanceof Lightable lightable) {
            lightable.setLit(blockData.isLit());
            block.setBlockData(lightable);
        }
        if (block.getBlockData() instanceof Openable openable) {
            openable.setOpen(blockData.isOpen());
            block.setBlockData(openable);
        }
        if (block.getBlockData() instanceof Orientable orientable) {
            orientable.setAxis(blockData.getAxis());
            block.setBlockData(orientable);
        }
        if (block.getBlockData() instanceof Powerable powerable) {
            powerable.setPowered(blockData.isPowered());
            block.setBlockData(powerable);
        }
        if (block.getBlockData() instanceof Rail rail) {
            rail.setShape(blockData.getShape());
            block.setBlockData(rail);
        }
        if (block.getBlockData() instanceof Rotatable rotatable) {
            rotatable.setRotation(blockData.getRotation());
            block.setBlockData(rotatable);
        }
        if (block.getBlockData() instanceof Snowable snowable) {
            snowable.setSnowy(blockData.isSnowy());
            block.setBlockData(snowable);
        }
        if (block.getBlockData() instanceof Waterlogged waterlogged) {
            waterlogged.setWaterlogged(blockData.isWaterlogged());
            block.setBlockData(waterlogged);
        }
        if (block.getBlockData() instanceof Bamboo bamboo) {
            bamboo.setLeaves(blockData.getBambooLeaves());
            block.setBlockData(bamboo);
        }
        if (block.getBlockData() instanceof PinkPetals pinkPetals) {
            pinkPetals.setFlowerAmount(blockData.getFlowerAmount());
            block.setBlockData(pinkPetals);
        }
    }
}
