package com.example.rabbitmq

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException



class MainActivity : AppCompatActivity() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var logInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pref = getSharedPreferences("key", MODE_PRIVATE)
        var token = pref.getString("id","EMPTY")
        var username = pref.getString("user","EMPTY")
        println(pref.getString("id","EMPTY"))
        var password = ""
        var login = ""
        setContentView(R.layout.activity_main)
        loginEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        logInButton = findViewById(R.id.button_id)

        logInButton.setOnClickListener {

            password = passwordEditText.text.toString()
            login = loginEditText.text.toString()

            val payload = "test payload"
            val requestBody = payload.toRequestBody()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://10.0.2.2:34778/api/home/login?username=$login&password=$password")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            throw IOException("Запрос к серверу не был успешен:" +
                                    " ${response.code} ${response.message}")
                        }
                        // пример получения всех заголовков ответа
                        for ((name, value) in response.headers) {
                            //println("$name: $value")
                        }
                        // вывод тела ответа
                        var jsonString = response.body?.string()
                        var json = JSONTokener(jsonString).nextValue() as JSONObject
//                        println(json.getString("token"))

                        if (response.code == 200){
                            var edit = pref.edit()
                            edit.putString("id",json.getString("token"))
                            edit.putString("user",json.getString("username"))
                            edit.apply()
                            val i = Intent(baseContext,  MainActivity2::class.java)
                            i.putExtra("key", login)
                            startActivity(i)
                        }
                    }
                }
            })
        }

        if (token!="EMPTY"){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://10.0.2.2:34778/api/home/checkauth?token=$token")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            throw IOException("Запрос к серверу не был успешен:" +
                                    " ${response.code} ${response.message}")
                        }
                        // пример получения всех заголовков ответа
                        for ((name, value) in response.headers) {
                            //println("$name: $value")
                        }
                        // вывод тела ответа
                        if (response.code == 200){
                            val i = Intent(baseContext,  MainActivity2::class.java)
                            i.putExtra("key", username)
                            startActivity(i)
                        }
                    }
                }
            })
        }

    }
}