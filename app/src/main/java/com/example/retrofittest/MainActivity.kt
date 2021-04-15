package com.example.retrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Res = findViewById(R.id.text_view_result) as TextView

       //logging  https://stackoverflow.com/questions/32514410/logging-with-retrofit-2
        val Logger = HttpLoggingInterceptor()
        Logger.setLevel(HttpLoggingInterceptor.Level.BODY)
        val Client = OkHttpClient.Builder().addInterceptor(Logger).build()

        val RF: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.102/Backend/LocationService.svc/") //
            .client(Client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Res.setText(RF.baseUrl().toString() + System.lineSeparator())

        val SRV = RF.create(JsonPlaceholder::class.java)

        val CityList = SRV.GetCities()

        //another way -  https://stackoverflow.com/questions/43233025/use-retrofit-methods-more-expressive-way
        CityList.enqueue(object : Callback<MutableList<OneItem>> {
            override fun onFailure(call: Call<MutableList<OneItem>>?, t: Throwable?) {
                if (t != null) {
                    Res.setText(t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<MutableList<OneItem>>?,
                response: Response<MutableList<OneItem>>?
            ) {
                if (response != null) {
                    if (!response.isSuccessful) {
                        Res.setText(response.code().toString())
                        return
                    } else {
                        val DT = response.body()
                        if (DT != null) {
                            val Str1=StringBuilder()
                            //or more correctly StringBuilder
                            DT.forEach { one -> Str1.append( "${one.City}(${one.Country})${System.lineSeparator()}" )}
                            Res.setText(Str1.toString())
                        }
                    }
                }
            }
        })
    }
}