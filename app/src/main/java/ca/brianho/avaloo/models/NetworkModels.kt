package ca.brianho.avaloo.models

import com.squareup.moshi.Json

enum class MessageType {
    CREATE,
    JOIN,
    START,
    PRE_GAME_INFO,
    FILTERED_ROLES,
    PARTY_CHOICE,
    PARTY_VOTE,
    PARTY_RESULT,
    CLIENT_SETUP,
    QUEST_INFO,
    QUEST_VOTE,
    QUEST_RESULT,
    CHOOSE_TARGET,
    GAME_OVER
}

data class CreateGameRequest(@Json(name = "type") val type: String = MessageType.CREATE.toString(),
                             @Json(name = "player") val player: Player)

data class CreateGameResponse(@Json(name = "gameId") val gameId: String,
                              @Json(name = "minNumPlayers") val minNumPlayers: Int)

data class JoinGameRequest(@Json(name = "type") val type: String = MessageType.JOIN.toString(),
                           @Json(name = "player") val player: Player,
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
                              @Json(name = "members") val members: Set<Player>)

data class ClientSetupResponse(@Json(name = "role") val role: Role1,
                               @Json(name = "playerList") val playerList: List<Player>)

data class FilteredRoleRequest(@Json(name = "type") val type: String = MessageType.FILTERED_ROLES.name,
                               @Json(name = "gameId") val gameId: String,
                               @Json(name = "roles") val roles: Set<String>)

data class QuestInfoResponse(@Json(name = "questNum") val questNum: Int,
                             @Json(name = "partySize") val partySize: Int,
                             @Json(name = "questDeclines") val questDeclines: Int,
                             @Json(name = "questLeader") val questLeader: Player)

data class PartyVoteRequest(@Json(name = "type") val type: String = MessageType.PARTY_VOTE.name,
                            @Json(name = "gameId") val gameId: String,
                            @Json(name = "vote") val vote: String)

data class PartyVoteResponse(@Json(name = "playerList") val playerList: List<Player>)

data class QuestVoteRequest(@Json(name = "type") val type: String = MessageType.QUEST_VOTE.name,
                            @Json(name = "gameId") val gameId: String,
                            @Json(name = "vote") val vote: String)

data class QuestResultResponse(@Json(name = "result") val result: String)

data class ChooseTargetResponse(@Json(name = "playerList") val playerList: List<Player>)

data class ChooseTargetRequest(@Json(name = "type") val type: String = MessageType.CHOOSE_TARGET.name,
                               @Json(name = "gameId") val gameId: String,
                               @Json(name = "player") val player: Player)

data class GameOverResponse(@Json(name = "winningTeam") val winningTeam: String,
                            @Json(name = "playerList") val playerList: Map<String, String>)