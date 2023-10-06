/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Bamboo;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class BlockData {

    @NotNull
    private final Material material;

    private boolean hasTags = false;

    private org.bukkit.block.data.BlockData originData;

    // Shared data
    private int age;
    private int power;
    private boolean attached;
    private Bisected.Half half;
    private int dusted;
    private BlockFace facing;
    private FaceAttachable.AttachedFace attachedFace;
    private boolean isHanging;
    private int hatch;
    private int level;
    private boolean lit;
    private boolean open;
    private Axis axis;
    private boolean powered;
    private Rail.Shape shape;
    private BlockFace rotation;
    private boolean snowy;
    private boolean waterlogged;

    // Specific data for specific blocks
    private Bamboo.Leaves bambooLeaves;
    private int flowerAmount;

    public BlockData(@NotNull Material material, @NotNull org.bukkit.block.data.BlockData originData) {
        this.material = material;
        this.originData = originData;
    }
}
