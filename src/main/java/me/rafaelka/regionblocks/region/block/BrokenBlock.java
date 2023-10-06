/*
 * Copyright (c) 2023 Rafaelka
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.rafaelka.regionblocks.region.block;

import org.bukkit.Location;
import org.bukkit.Particle;

public record BrokenBlock(Location location, BlockData blockData, int priority, boolean useRegenParticle,
                          Particle regenParticleType, int regenParticleCount, double regenParticleExtra) {
}
