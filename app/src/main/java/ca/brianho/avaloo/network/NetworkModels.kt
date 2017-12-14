package ca.brianho.avaloo.network

import com.squareup.moshi.Json

enum class MessageType {
    CREATE,
    JOIN,
    START,
    PRE_GAME_INFO,
    FILTERED_ROLES,
    PARTY_CHOICE,
    PARTY_VOTE
}

data class CreateGameRequest(@Json(name = "type") val type: String = MessageType.CREATE.toString(),
                             @Json(name = "player") val player: Player)

data class CreateGameResponse(@Json(name = "gameId") val gameId: String,
                              @Json(name = "minNumPlayers") val minNumPlayers: Int)

data class JoinGameRequest(@Json(name = "type") val type: String = MessageType.JOIN.toString(),
                           @Json(name = "playerId") val playerId: String,
                           @Json(name = "alias") val alias: String,
                           @Json(name = "gameId") val gameId: String)

data class JoinGameResponse(@Json(name = "gameState") val gameState: String)

data class PlayerJoinResponse(@Json(name = "player") val player: Player)

data class StartGameRequest(@Json(name = "type") val type: String = MessageType.START.toString(),
                            @Json(name = "gameId") val gameId: String,
                            @Json(name = "playerOrder") val playerOrder: MutableList<Player>)

data class StartGameResponse(@Json(name = "numGood") val numGood: Int,
                             @Json(name = "numEvil") val numEvil: Int,
                             @Json(name = "roles") val roles: List<Role>)

data class PartyChoiceRequest(@Json(name = "type") val type: String = MessageType.PARTY_CHOICE.name,
                              @Json(name = "gameId") val gameId: String,
                              @Json(name = "members") val members: MutableSet<Player>)

data class ClientSetupResponse(@Json(name = "role") val role: Role1,
                               @Json(name = "playerList") val playerList: List<Player>)