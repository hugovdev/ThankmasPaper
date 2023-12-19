package me.hugo.thankmas.player

import dev.kezz.miniphrase.MiniPhraseContext
import fr.mrmicky.fastboard.adventure.FastBoard
import me.hugo.thankmas.scoreboard.getOrCreateTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.util.*

public open class ScoreboardPlayerData(playerUUID: UUID) : PaperPlayerData(playerUUID) {

    private var board: FastBoard? = null
    public var playerNameTag: PlayerNameTag? = null
        set(tag) {
            field = tag

            if (tag == null) return
            Bukkit.getOnlinePlayers().forEach { tag.apply(it) }
        }

    context(MiniPhraseContext)
    public open fun initializeBoard(title: String? = null, locale: Locale? = null, player: Player? = null): Player {
        val finalPlayer = player ?: onlinePlayer

        finalPlayer.scoreboard = Bukkit.getScoreboardManager().newScoreboard

        val board = FastBoard(finalPlayer)
        title?.let { board.updateTitle(miniPhrase.translate(title, locale ?: finalPlayer.locale())) }

        this.board = board

        return finalPlayer
    }

    /** @returns the player's FastBoard instance, can be null. */
    public fun getBoardOrNull(): FastBoard? {
        return board
    }

    /** @returns the player's FastBoard instance. */
    public fun getBoard(): FastBoard {
        val board = getBoardOrNull()
        requireNotNull(board) { "Tried to fetch a player's fast board while its null." }

        return board
    }

    public class PlayerNameTag(
        private val owner: UUID,
        private val namedTextColor: ((viewer: Player, preferredLocale: Locale?) -> NamedTextColor)? = null,
        private val prefixSupplier: ((viewer: Player, preferredLocale: Locale?) -> Component)? = null,
        private val suffixSupplier: ((viewer: Player, preferredLocale: Locale?) -> Component)? = null,
        private val belowNameSupplier: ((viewer: Player, preferredLocale: Locale?) -> Component)? = null
    ) {

        /**
         * Adds this player name tag to [viewer]'s scoreboard.
         */
        public fun apply(viewer: Player, preferredLocale: Locale? = null) {
            val playerOwner = owner.player() ?: return

            val id = owner.toString()
            val team = viewer.scoreboard.getOrCreateTeam(id)

            team.prefix(prefixSupplier?.let { it(viewer, preferredLocale) })
            team.suffix(suffixSupplier?.let { it(viewer, preferredLocale) })
            team.color(namedTextColor?.let { it(viewer, preferredLocale) })

            val entry = playerOwner.name
            if (!team.hasEntry(entry)) team.addEntry(entry)

            if (belowNameSupplier == null) return
        }

        /**
         * Updates the player name tag for everyone.
         */
        public fun updateForAll() {
            Bukkit.getOnlinePlayers().forEach { apply(it) }
        }

        /**
         * Removes this player name tag from [scoreboard].
         */
        public fun remove(scoreboard: Scoreboard) {
            val id = owner.toString()

            scoreboard.getTeam(id)?.unregister()
            // scoreboard.getObjective(id)?.unregister()
        }

    }

}