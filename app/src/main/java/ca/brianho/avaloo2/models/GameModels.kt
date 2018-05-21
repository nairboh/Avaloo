package ca.brianho.avaloo2.models

import com.squareup.moshi.Json

enum class Team { GOOD, EVIL }
enum class QuestState { PASS, FAIL }

data class Player(@Json(name = "alias") val alias: String,
                  @Json(name = "playerId") val playerId: String)

data class Role1(@Json(name = "name") val name: String,
                @Json(name = "team") val team: String,
                @Json(name = "knowledge") val knowledge: List<String>)


data class Role(@Json(name = "name") val name: String,
                @Json(name = "team") val team: String)

data class Quest(@Json(name = "questNum") val questNum: Int,
                 @Json(name = "questLead") val questLead: Player,
                 @Json(name = "questState") val questState: String,
                 @Json(name = "partyMembers") val partyMembers: List<Player>)


object Game {
    lateinit var gameId: String
    lateinit var currentPlayer: Player
    var minNumPlayers = 5

    lateinit var currentRole: Role
    lateinit var playerList: List<Player>
    val questList: MutableList<Quest> = mutableListOf()
}
