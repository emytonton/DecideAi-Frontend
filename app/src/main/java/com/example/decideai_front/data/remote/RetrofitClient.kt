package com.example.decideai_front.data.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Substitua pelo IP da sua máquina ou URL do Heroku/Render
    // Se estiver usando o emulador do Android, use 10.0.2.2 para acessar o localhost do seu PC
    private const val BASE_URL = "http://10.0.2.2:3333/"

    val instance: DecideAiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DecideAiService::class.java)
    }
}