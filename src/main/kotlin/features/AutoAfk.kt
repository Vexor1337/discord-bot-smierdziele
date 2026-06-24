package features

import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import kotlinx.coroutines.delay

// Usunęliśmy zmyślone ID. Bot sam znajdzie kanał AFK.
suspend fun Kord.setupAutoAfk() {
    on<VoiceStateUpdateEvent> {
        val before = old
        val after = state

        val wasMuted = before?.isSelfMuted ?: false
        val isMuted = after.isSelfMuted

        if (!wasMuted && isMuted) {
            val member = after.getMember()
            delay(1000)
            val currentVoiceState = member.getVoiceStateOrNull()

            if (currentVoiceState != null && currentVoiceState.isSelfMuted) {
                try {
                    val guild = after.getGuild()
                    val afkChannelId = guild.afkChannelId

                    if (afkChannelId != null) {
                        member.edit { voiceChannelId = afkChannelId }
                        println("Moved ${member.effectiveName}!")
                    } else {
                        println("No AFK channel found!")
                    }
                } catch (e: Exception) {
                    println(" Exception while moving (${e.message})")
                }
            }
        }
    }
}