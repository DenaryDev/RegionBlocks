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
import org.bukkit.Location;
import org.bukkit.Particle;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@RequiredArgsConstructor
public class BrokenBlock {
    private final Location location;
    private final BlockData blockData;
    private final int priority;
    private final Particle regenParticleType;
    private final int regenParticleCount;
}
