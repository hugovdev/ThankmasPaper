package me.hugo.thankmas.player.rank

import me.hugo.thankmas.player.PlayerDataManager
import me.hugo.thankmas.player.player
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.event.node.NodeMutateEvent
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player

/**
 * Listens to any group changes made by LuckPerms and updates
 * the player's name tag and sidebar.
 */
public class PlayerGroupChange<P : RankedPlayerData>(
    private val playerManager: PlayerDataManager<P>,
    extraActions: (player: Player) -> Unit
) {

    init {
        val luckPerms = LuckPermsProvider.get()

        // Player rank changes so we update their name tags!
        luckPerms.eventBus.subscribe(NodeMutateEvent::class.java) { event ->
            val userId = (event.target as? User?)?.uniqueId ?: return@subscribe
            val onlinePlayer = userId.player() ?: return@subscribe

            playerManager.getPlayerData(userId).playerNameTag?.updateForAll()
            extraActions(onlinePlayer)
        }
    }

}