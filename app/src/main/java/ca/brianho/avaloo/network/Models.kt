package ca.brianho.avaloo.network

import com.squareup.moshi.Json

data class StartGameRequest (@Json(name = "playerId") val playerId: String,
                             @Json(name = "alias") val alias: String)

data class StartGameResponse (val gameId: String)

data class JoinGameRequest (@Json(name = "playerId") val playerId: String,
                            @Json(name = "alias") val alias: String,
                            @Json(name = "gameId") val gameId: String)

data class JoinGameResponse (val gameState: String)