package ca.brianho.avaloo.network

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

data class Game(@Json(name = "playerList") val playerList: List<Player>,
                @Json(name = "questList") val questList: MutableList<Quest>,
                @Json(name = "currentPlayer") val currentPlayer: Player,
                @Json(name = "currentRole") val currentRole: Role,
                @Json(name = "gameId") val gameId: String)