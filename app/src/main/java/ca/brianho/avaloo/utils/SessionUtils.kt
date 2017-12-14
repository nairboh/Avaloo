package ca.brianho.avaloo.utils

import ca.brianho.avaloo.fragments.game.BoardFragment
import ca.brianho.avaloo.fragments.game.RoleFragment
import ca.brianho.avaloo.network.*
import com.squareup.moshi.Moshi
import okhttp3.WebSocket

lateinit var name: String
lateinit var playerId: String

lateinit var player: Player

lateinit var roles: MutableList<Role>

var numGood = 0
var numEvil = 0

lateinit var gameInstance: Game

lateinit var createGameResponse: CreateGameResponse

lateinit var boardFragment: BoardFragment
lateinit var roleFragment: RoleFragment

lateinit var playerMap: Map<String, String>

lateinit var websocket: WebSocket
lateinit var wsListener: WSListener
lateinit var moshi: Moshi
