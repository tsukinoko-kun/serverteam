package de.kuerbisskraft.serverteam

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.logging.Level
import java.util.logging.Logger

class ServerTeamManager(scoreboard: Scoreboard, logger: Logger, teamName: String) {
    // Permission error message
    private val permissionError = "${ChatColor.RED}Du hast nicht die nötige Berechtigung!"

    // init Team
    private var team: Team = if (scoreboard.getTeam(teamName) != null) {
        scoreboard.getTeam(teamName)!!
    } else {
        logger.log(Level.INFO, "registering new team $teamName")
        scoreboard.registerNewTeam(teamName)
    }

    // Mothods for onPlayerJoin Event

    fun isMember(player: String): Boolean {
        return team.hasEntry(player)
    }

    fun broadcastMessage(message:String) {
        for (playerName in list()) {
            Bukkit.getPlayer(playerName)?.sendMessage(message)
        }
    }

    // Methods called by CmdInterpreter

    fun askAdd(sender: CommandSender, player: String): Boolean {
        if (!sender.hasPermission("serverteam.add")) {
            sender.sendMessage(permissionError)
            return false
        }
        add(player)
        sender.sendMessage("${ChatColor.GREEN}Spieler $player wurde zum Team hinzugefügt")
        return true
    }

    fun askDelete(sender: CommandSender, player: String): Boolean {
        if (!sender.hasPermission("serverteam.delete")) {
            sender.sendMessage(permissionError)
            return false
        }

        if (delete(player)) {
            sender.sendMessage("${ChatColor.GREEN}Spieler $player wurde aus dem Team entfernt")
        } else {
            sender.sendMessage("${ChatColor.RED}Spieler $player konnte nicht entfernt werden")
        }
        return true
    }

    fun askList(sender: CommandSender): Boolean {
        if (!sender.hasPermission("serverteam.list")) {
            sender.sendMessage(permissionError)
            return false
        }

        val list = list()
        if (list.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}Keine Spieler im Team")
        } else {
            sender.sendMessage("${ChatColor.GOLD}${list.size} Spieler im Team: \n${ChatColor.AQUA}" + list().joinToString("\n"))
        }
        return true
    }

    fun askSetPrefix(sender: CommandSender, prefix: String): Boolean {
        if (!sender.hasPermission("serverteam.edit")) {
            sender.sendMessage(permissionError)
            return false
        }

        setPrefix(prefix)
        sender.sendMessage("Neuer Prefix: $prefix")
        return true
    }

    // Data get methods

    private fun add(player: String) {
        team.addEntry(player)
    }

    private fun delete(player: String): Boolean {
        return team.removeEntry(player)
    }

    private fun list(): List<String> {
        return team.entries.toList()
    }

    private fun setPrefix(prefix: String) {
        team.prefix = prefix
    }
}
