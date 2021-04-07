package de.kuerbisskraft.serverteam

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class ServerTeam : JavaPlugin(), Listener, CommandExecutor {
    private lateinit var serverTeamManager: ServerTeamManager
    private lateinit var cmdInterpreter: CmdInterpreter

    override fun onEnable() {
        serverTeamManager = ServerTeamManager(Bukkit.getScoreboardManager()?.mainScoreboard!!, Bukkit.getLogger(), "ServerTeam")
        cmdInterpreter = CmdInterpreter(serverTeamManager)
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
        val name = event.player.name
        if (serverTeamManager.isMember(name)) {
            serverTeamManager.broadcastMessage("${ChatColor.YELLOW}Das Teammitglied ${ChatColor.GREEN}${name}${ChatColor.YELLOW} ist jetzt ${ChatColor.GREEN}online")
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        val name = event.player.name
        if (serverTeamManager.isMember(name)) {
            serverTeamManager.broadcastMessage("${ChatColor.YELLOW}Das Teammitglied ${ChatColor.GREEN}${name}${ChatColor.YELLOW} ist jetzt ${ChatColor.RED}offline")
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name == "serverteam") {
            return cmdInterpreter.onCommand(sender, args)
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (command.name != "serverteam") {
            return mutableListOf()
        }

        return cmdInterpreter.onTabComplete(sender, args)
    }
}
