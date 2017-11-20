package ca.brianho.avaloo.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AvalooAPI {

    @POST("/start_game")
    fun startGame(@Body request: StartGameRequest) : Single<StartGameResponse>

    @POST("/join_game")
    fun joinGame(@Body request: JoinGameRequest) : Single<JoinGameResponse>

    companion object Factory {
        fun create() : AvalooAPI {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .baseUrl("http://192.168.0.15:5000")
                    .build()

            return retrofit.create(AvalooAPI::class.java)
        }
    }
}