package ca.brianho.avaloo.utils

import ca.brianho.avaloo.network.CreateGameResponse
import ca.brianho.avaloo.network.Role
import com.squareup.moshi.Moshi
import okhttp3.WebSocket

lateinit var name: String
lateinit var playerId: String

lateinit var roles: MutableList<Role>

var numGood = 0
var numEvil = 0

lateinit var createGameResponse: CreateGameResponse

lateinit var websocket: WebSocket
lateinit var moshi: Moshi
