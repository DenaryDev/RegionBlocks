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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class RegionBlocksCommand implements CommandExecutor, TabCompleter {

    private final RegionBlocksPlugin plugin;

    public RegionBlocksCommand(RegionBlocksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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
                    //noinspection SpellCheckingInspection
                    case "regenall" -> {
                        //noinspection SpellCheckingInspection
                        if (args.length == 1 && sender.hasPermission("regionblocks.command.regenall")) {
                            regionManager.regenAllRegions();
                            sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eВсе регионы успешно регенерированы!");
                            return true;
                        }
                    }
                    case "shutdown" -> {
                        if (args.length == 1 && sender.hasPermission("regionblocks.command.shutdown")) {
                            regionManager.regenAllRegions();
                            sender.sendMessage("§bRegionBlocks §8| §aInfo §8> §eВсе регионы успешно регенерированы, останавливаю сервер...");
                            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getServer().shutdown(), 1L);
                            return true;
                        }
                    }
                }
            }

            sender.sendMessage("§bRegionBlocks §8| §eПомощь по команде /regionblocks (/rb)");
            sender.sendMessage("§7§oАргументы в <> обязательны, а в [] не обязательны.");
            sender.sendMessage("§8§l* §6/rb reload §7- §eПерезагружает плагин.");
            //sender.sendMessage("§e/regionblocks toggle §7- §fПереключает ломание блоков.");
            sender.sendMessage("§8§l* §6/rb regen [регион] §7- §eРегенерирует указанный регион или регион, в котором вы находитесь");
            //noinspection SpellCheckingInspection
            sender.sendMessage("§8§l* §6/rb regenall §7- §eРегенерирует все регионы.");
            sender.sendMessage("§8§l* §6/rb shutdown §7- §eРегенерирует все регионы и останавливает сервер.");
            sender.sendMessage(" ");
        }
        return true;
    }

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> aliases = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].isEmpty()) {
                if (sender.hasPermission("regionblocks.command.reload")) aliases.add("reload");
                if (sender.hasPermission("regionblocks.command.regen")) aliases.add("regen");
                if (sender.hasPermission("regionblocks.command.regenall")) aliases.add("regenall");
                if (sender.hasPermission("regionblocks.command.shutdown")) aliases.add("shutdown");
            } else {
                String arg = args[0];
                if ("reload".contains(arg) && sender.hasPermission("regionblocks.command.reload")) aliases.add("reload");
                if ("regen".contains(arg) && sender.hasPermission("regionblocks.command.regen")) aliases.add("regen");
                if ("regenall".contains(arg) && sender.hasPermission("regionblocks.command.regenall")) aliases.add("regenall");
                if ("shutdown".contains(arg) && sender.hasPermission("regionblocks.command.shutdown")) aliases.add("shutdown");
            }
        } else if (args.length == 2) {
            if (args[1].isEmpty()) {
                if (sender.hasPermission("regionblocks.command.regen")) {
                    for (Region region : plugin.getRegionManager().getRegions()) {
                        aliases.addAll(region.getNames());
                    }
                }
            } else {
                String arg = args[1];
                if (sender.hasPermission("regionblocks.command.regen")) {
                    for (Region region : plugin.getRegionManager().getRegions()) {
                        for (String name : region.getNames()) {
                            if (name.startsWith(arg)) aliases.add(name);
                        }
                    }
                }
            }
        }

        return aliases;
    }
}
