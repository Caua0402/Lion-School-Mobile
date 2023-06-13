package br.senai.sp.jandira.lionschool.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "https://lionschool-api.cyclic.app/v1/lion-school/"

    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getCursosService(): CursosService {
        return retrofitFactory.create(CursosService::class.java)
    }

    fun getAlunosService(): AlunosService {
        return retrofitFactory.create(AlunosService::class.java)
    }

}