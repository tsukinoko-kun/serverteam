package de.kuerbisskraft.serverteam

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CmdInterpreter(private val teamManager: ServerTeamManager) {
    fun onCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            return false
        }

        when (args[0]) {
            "add" -> {
                if (args.size >= 2) {
                    return teamManager.askAdd(sender, args[1])
                } else if (sender is Player) {
                    return teamManager.askAdd(sender, sender.name)
                }
            }

            "delete" -> {
                if (args.size >= 2) {
                    return teamManager.askDelete(sender, args[1])
                } else if (sender is Player) {
                    return teamManager.askDelete(sender, sender.name)
                }
            }

            "list" -> {
                teamManager.askList(sender)
            }

            "prefix" -> {
                if (args.size >= 2) {
                    return teamManager.askSetPrefix(sender, args[1])
                }
            }
        }

        return false
    }

    fun onTabComplete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        when (args.size) {
            1 -> {
                return mutableListOf("add", "delete", "list", "prefix")
            }

            2 -> {
                when (args[0]) {
                    "add", "delete" -> {
                        val out = mutableListOf<String>()
                        for (player in Bukkit.getOnlinePlayers()) {
                            out.add(player.name)
                        }
                        return out
                    }

                    "prefix" -> {
                        return mutableListOf("[ServerTeam] ")
                    }
                }
            }
        }
        return mutableListOf()
    }
}
