import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import features.setupAutoAfk
import features.setupPokeCommand

suspend fun main() {
    val token = "xxx"

    val kord = Kord(token) //token for discord api


    kord.setupAutoAfk()
    kord.setupPokeCommand()

    println("Logowanie bota...")
    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.GuildVoiceStates
    }
}