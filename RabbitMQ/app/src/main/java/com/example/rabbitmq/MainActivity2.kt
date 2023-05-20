package com.example.rabbitmq

import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.rabbitmq.databinding.ActivityMain2Binding
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.StandardCharsets
import kotlin.math.log

class MainActivity2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val login = intent.getStringExtra("key")

//        println(login)

        Thread(Runnable {
//            val factory = ConnectionFactory()

            val factory = ConnectionFactory()
                .apply {
                    username = "guest"
                    password = "guest"
                    host = "10.0.2.2"
                }

            val connection = factory.newConnection()
            val channel = connection.createChannel()
            val consumerTag = "SimpleConsumer"

            channel.queueDeclare("infinity", false, false, true, null)


            println("[$consumerTag] Waiting for messages...")
            val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
                val message = String(delivery.body, StandardCharsets.UTF_8)
                println("[$consumerTag] Received message: '$message'")

            }
            val cancelCallback = CancelCallback { consumerTag: String? ->
                println("[$consumerTag] was canceled")
            }

            channel.basicConsume("infinity", true, consumerTag, deliverCallback, cancelCallback)
        }).start()

        Thread(Runnable {
//            val factory = ConnectionFactory()
            var x = false
            val factory = ConnectionFactory()
                .apply {
                    username = "guest"
                    password = "guest"
                    host = "10.0.2.2"
                }

            val connection = factory.newConnection()
            val channel = connection.createChannel()
            val consumerTag = "LogoutConsumer"

            channel.queueDeclare("logout", false, false, true, null)


            println("[$consumerTag] Waiting for messages...")
            val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
                val message = String(delivery.body, StandardCharsets.UTF_8)
                println("[$consumerTag] Received message: '$message'")
                if (message == login && x==true){
                    println("EXIIIIIIIIIT")
                    finish()
                } else{
                    x=true
                }
            }
            val cancelCallback = CancelCallback { consumerTag: String? ->
                println("[$consumerTag] was canceled")
            }

            channel.basicConsume("logout", true, consumerTag, deliverCallback, cancelCallback)
        }).start()

    }


}