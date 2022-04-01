/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.regionblocks.commands;

import io.sapphiremc.regionblocks.region.Region;
import io.sapphiremc.regionblocks.RegionBlocksPlugin;
import io.sapphiremc.regionblocks.region.RegionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionBlocksCommand implements CommandExecutor {

    private final RegionBlocksPlugin plugin;

    public RegionBlocksCommand(RegionBlocksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("regionblocks.command.use")) {
            if (args.length > 0) {
                RegionManager regionManager = plugin.getRegionManager();
                switch (args[0]) {
                    case "reload" -> {
                        if (args.length == 1 && sender.hasPermission("regionblocks.command.reload")) {
                            plugin.onReload();
                            sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eПлагин успешно перезагружен!");
                            return true;
                        }
                    }
                    case "toggle" -> {
                        if (sender instanceof Player player) {
                            if (args.length == 1 && sender.hasPermission("regionblocks.command.toggle")) {
                                if (regionManager.canBreak(player)) {
                                    regionManager.setCanBreak(player, false);
                                    sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eВы больше не можете ломать блоки!");
                                } else {
                                    regionManager.setCanBreak(player, true);
                                    sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eВы теперь можете ломать блоки!");
                                }
                                return true;
                            }
                        } else {
                            sender.sendMessage("§bRegionBlocks §8| §cError §8> §eЭту команду могут использовать только игроки!");
                            return true;
                        }
                    }
                    case "regen" -> {
                        if (sender.hasPermission("regionblocks.command.regen")) {
                            if (args.length == 1) {
                                if (sender instanceof Player player) {
                                    Region region = regionManager.getRegionAtLocation(player.getLocation());
                                    if (region != null) {
                                        regionManager.regenRegion(region);
                                        sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eРегион успешно регенерирован!");
                                    } else {
                                        sender.sendMessage("§bRegionBlocks §8| §cError §8> §eВы должны находиться в регионе или указать его название!");
                                    }
                                } else {
                                    sender.sendMessage("§bRegionBlocks §8| §cError §8> §eВы должны быть игроком, чтобы регенерировать регион, в котором находитесь!");
                                }
                                return true;
                            } else if (args.length == 2) {
                                Region region = regionManager.getRegionByName(args[1]);
                                if (region != null) {
                                    regionManager.regenRegion(region);
                                    sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eРегион успешно регенерирован!");
                                } else {
                                    sender.sendMessage("§bRegionBlocks §8| §cError §8> §eРегион с названием " + args[1] + " не найден!");
                                }
                                return true;
                            }
                        }
                    }
                    case "regenall" -> {
                        if (args.length == 1 && sender.hasPermission("regionblocks.command.regenall")) {
                            regionManager.regenAllRegions();
                            sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eВсе регионы успешно регенерированы!");
                            return true;
                        }
                    }
                }
            }
            sender.sendMessage("§bRegionBlocks §8| §6Помощь по команде /regionblocks (/rb)");
            sender.sendMessage("§7§oАгрументы в <> обязательны, а в [] не обязательны.");
            sender.sendMessage("§e/rb reload §7- §fПерезагружает плагин.");
            //sender.sendMessage("§e/regionblocks toggle §7- §fПереключает ломание блоков.");
            sender.sendMessage("§e/rb regen [регион] §7- §fРегенерирует указанный регион или регион, в котором вы находитесь");
            sender.sendMessage("§e/rb regenall §7- §fРегенерирует все регионы.");
            sender.sendMessage(" ");
        }
        return false;
    }
}
