package features

import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.user
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun Kord.setupPokeCommand() {
    createGlobalChatInputCommand(
        "poke",
        "poke."
    ) {
        user("target", "target") {
            required = true
        }
    }

    on<ChatInputCommandInteractionCreateEvent> {
        val command = interaction.command
        if (command.rootName == "poke") {
            val response = interaction.deferPublicResponse()

            val targetUser = command.users["target"] ?: return@on
            val guild = interaction.data.guildId.value?.let { getGuildOrNull(it) } ?: return@on
            val member = targetUser.asMember(guild.id)
            val voiceState = member.getVoiceStateOrNull()

            if (voiceState == null || voiceState.channelId == null) {
                response.respond { content = "${member.effectiveName} is offline!" }
                return@on
            }

            response.respond { content = "jebac cie ${member.effectiveName}! 🚀👻" }

            val voiceChannels = guild.channels.filterIsInstance<VoiceChannel>().toList()
            val textChannels = guild.channels.filterIsInstance<TextChannel>().toList()
            val originalChannel = voiceState.channelId!!

            launch {
                val startTime = System.currentTimeMillis()

                val voiceJob = launch {
                    var channelIndex = 0
                    while (System.currentTimeMillis() - startTime < 10000) {
                        val currentVoiceState = member.getVoiceStateOrNull()
                        if (currentVoiceState == null || currentVoiceState.channelId == null) break

                        val nextChannel = voiceChannels[channelIndex % voiceChannels.size]
                        try {
                            member.edit { voiceChannelId = nextChannel.id }
                            channelIndex++
                        } catch (e: Exception) {
                        }

                        delay(1000)
                    }
                }

                val pingJob = launch {
                    while (System.currentTimeMillis() - startTime < 10000) {
                        if (textChannels.isNotEmpty()) {
                            val randomTextChannel = textChannels.random()

                            launch {
                                try {
                                    val msg = randomTextChannel.createMessage(member.mention)

                                    withContext(NonCancellable) {
                                        msg.delete()
                                    }
                                } catch (e: Exception) {
                                    println("Ghost Ping exception: ${e.message}")
                                }
                            }
                        }

                        delay(600)
                    }
                }

                delay(10000)

                voiceJob.cancel()
                pingJob.cancel()

                try {
                    val finalState = member.getVoiceStateOrNull()
                    if (finalState != null && finalState.channelId != null) {
                        member.edit { voiceChannelId = originalChannel }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
}