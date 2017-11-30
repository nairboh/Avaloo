package ca.brianho.avaloo.network

import com.squareup.moshi.Json

enum class RequestTypes { CREATE, JOIN, START }

data class StartGameRequest (@Json(name = "type") val type: String,
                             @Json(name = "playerId") val playerId: String,
                             @Json(name = "alias") val alias: String)

data class StartGameResponse (val gameId: String)

data class JoinGameRequest (@Json(name = "type") val type: String,
                            @Json(name = "playerId") val playerId: String,
                            @Json(name = "alias") val alias: String,
                            @Json(name = "gameId") val gameId: String)

data class JoinGameResponse (val gameState: String)