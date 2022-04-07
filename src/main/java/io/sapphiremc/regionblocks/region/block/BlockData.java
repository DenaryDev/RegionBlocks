/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.region.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Axis;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bell;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.StructureBlock;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.block.data.type.Wall;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class BlockData {

    private final Material material;

    private boolean hasTags = false;

    // Shared data
    private int age;
    private int power;
    private boolean attached;
    private Bisected.Half half;
    private BlockFace facing;
    private BlockFace faceAttached;
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
    private Bed.Part bedPart;
    private int beeHiveHoneyLevel;
    private Bell.Attachment bellAttachment;
    private Set<Integer> brewingStandBottles;
    private boolean bubbleColumnDrag;
    private int cakeBites;
    private boolean campfireSignalFire;
    private Chest.Type chestType;
    private boolean commandBlockConditional;
    private Comparator.Mode comparatorMode;
    private boolean dayLightDetectorInverted;
    private boolean dispenserTriggered;
    private Door.Hinge doorHinge;
    private boolean endPortalFrameHasEye;
    private int farmlandMoisture;
    private boolean gateInWall;
    private boolean hopperEnabled;
    private Jigsaw.Orientation jigsawOrientation;
    private boolean jukeboxHasRecord;
    private boolean lanternHanging;
    private int leavesDistance;
    private boolean leavesPersistent;
    private boolean lecternHasBook;
    private Instrument noteBlockInstrument;
    private Note noteBlockNote;
    private boolean pistonExtended;
    private boolean pistonHeadShort;
    private RedstoneWire.Connection redstoneWireConnection;
    private int repeaterDelay;
    private boolean repeaterLocked;
    private int respawnAnchorCharges;
    private int saplingStage;
    private int scaffoldingDistance;
    private boolean scaffoldingBottom;
    private int seaPicklePickles;
    private Slab.Type slabType;
    private int snowLayers;
    private Stairs.Shape stairsShape;
    private StructureBlock.Mode structureBlockMode;
    private TechnicalPiston.Type technicalPistonType;
    private boolean tntUnstable;
    private boolean tripwireDisarmed;
    private int turtleEggsEggs;
    private int turtleEggsHatch;
    private Wall.Height wallHeight;
    private boolean wallUp;
}
