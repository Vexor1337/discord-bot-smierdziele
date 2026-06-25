import com.sun.net.httpserver.HttpServer
import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import features.setupAutoAfk
import features.setupPokeCommand
import java.net.InetSocketAddress

suspend fun main() {

    val port = System.getenv("PORT")?.toInt() ?: 10000
    val server = HttpServer.create(InetSocketAddress(port), 0)
    server.createContext("/") { exchange ->
        val response = "Bot is active!".toByteArray()
        exchange.sendResponseHeaders(200, response.size.toLong())
        exchange.responseBody.write(response)
        exchange.responseBody.close()
    }
    server.start()
    println("Serwer HTTP uruchomiony na porcie $port")

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