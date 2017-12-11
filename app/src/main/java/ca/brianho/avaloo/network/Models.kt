package ca.brianho.avaloo.network

import com.squareup.moshi.Json

enum class RequestTypes { CREATE, JOIN, START }

data class Player(@Json(name = "alias") val alias: String,
                  @Json(name = "playerId") val playerId: String)

data class Role(@Json(name = "name") val name: String,
                @Json(name = "team") val team: String)

data class CreateGameRequest(@Json(name = "type") val type: String,
                             @Json(name = "playerId") val playerId: String,
                             @Json(name = "alias") val alias: String)

data class CreateGameResponse(@Json(name = "gameId") val gameId: String,
                              @Json(name = "minNumPlayers") val minNumPlayers: Int)

data class JoinGameRequest(@Json(name = "type") val type: String,
                           @Json(name = "playerId") val playerId: String,
                           @Json(name = "alias") val alias: String,
                           @Json(name = "gameId") val gameId: String)

data class JoinGameResponse(@Json(name = "gameState") val gameState: String)