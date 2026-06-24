import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import features.setupAutoAfk
import features.setupPokeCommand

suspend fun main() {
    val token = System.getenv("DISCORD_TOKEN")
        ?: throw IllegalStateException("DISCORD_TOKEN not found")

    val kord = Kord(token) //token for discord api


    kord.setupAutoAfk()
    kord.setupPokeCommand()

    println("Logowanie bota...")
    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.GuildVoiceStates
    }
}