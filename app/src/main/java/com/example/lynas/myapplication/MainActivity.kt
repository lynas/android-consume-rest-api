package com.example.lynas.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import awaitObjectResponse
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.google.gson.Gson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        verticalLayout {

            button {
                text = "Get List"

            }.onClick {
                getPersonList()
            }

            button {
                text = "Get One"

            }.onClick {
                getPerson()
            }


            button {
                text = "Post"
            }.onClick {
                postPerson()
            }

            textView = textView {
                text = "Hello world"
            }
        }


    }

    private fun getPersonList() {
        async(UI) {
            val (request, response, result) = Fuel.get("http://192.168.1.2:8080/persons")
                    .awaitObjectResponse(gsonDeserializerOf<List<Person>>())
            result.fold({ data ->
                textView.text = "${data}"
            }, { error ->
                textView.text = error.localizedMessage
            })

        }
    }


    private fun getPerson() {
        async(UI) {
            val (request, response, result) = Fuel.get("http://192.168.1.2:8080/persons/1")
                    .awaitObjectResponse(gsonDeserializerOf<Person>())
            result.fold({ data ->
                textView.text = "${data}"
            }, { error ->
                textView.text = error.localizedMessage
            })

        }
    }

    private fun postPerson() {
        val header = mapOf("Content-Type" to "application/json")
        async(UI) {
            val (request, response, result) = Fuel.post("http://192.168.1.2:8080/persons")
                    .header(header)
                    .body(Gson().toJson(Person(6,"Flame",66)))
                    .awaitObjectResponse(gsonDeserializerOf<Person>())
            result.fold({ data ->
                textView.text = "${data}"
            }, { error ->
                textView.text = error.localizedMessage
            })

        }
    }


}



data class Person(val id:Int, val name:String, val age:Int)
