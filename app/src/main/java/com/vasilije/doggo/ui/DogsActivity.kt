package com.vasilije.doggo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vasilije.doggo.R
import com.vasilije.doggo.adapter.DogsAdapter
import com.vasilije.doggo.model.Doggo
import com.vasilije.doggo.model.Request
import com.vasilije.doggo.retrofit.DogsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class DogsActivity : AppCompatActivity() {

    private lateinit var dogsAdapter: DogsAdapter
    private lateinit var swipeDownToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dogsAdapter = DogsAdapter(applicationContext)
        swipeDownToRefresh = findViewById(R.id.swipeRefresh)
        val recyclerView: RecyclerView = findViewById(R.id.dogsRecycler)
        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = dogsAdapter

        // TODO base URL should be moved to a separate file
        val retrofit = Retrofit.Builder()
            .baseUrl(" https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DogsApi::class.java)
        val call = service.getDogsPictures();
        makeApiCall(call)

        swipeDownToRefresh.setOnRefreshListener {
            val newCall = service.getDogsPictures()
            swipeDownToRefresh.isRefreshing = makeApiCall(newCall)
        }

    }

    fun makeApiCall(call: Call<Request>): Boolean {
        var requestSuccess = false
        call.enqueue(object : Callback<Request> {
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                if (response.code() == 200) {

                    val listOfDogPhotos = response.body()?.message
                    val dogsList = listOfDogPhotos?.let { getDoggoList(it) }

                    if (dogsList != null && dogsList.isNotEmpty()) {

                        dogsAdapter.submitList(dogsList)
                        dogsAdapter.notifyDataSetChanged()
                        requestSuccess = true
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "We could not get your doggo pics :(",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("Response", t.message.toString())

                Toast.makeText(
                    applicationContext,
                    "We could not get your doggo pictures :( - Check if you have internet connection",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        return requestSuccess
    }

    // specific for the given API
    fun getDoggoList(listOfDogPhotos: List<String>): List<Doggo> {
        val dogsList = mutableListOf<Doggo>()
        for (link in listOfDogPhotos) {
            val singleDog = Doggo()

            singleDog.pictureUrl = link

            var breed = "breed"
            if (link.contains("/")) {
                breed = link.split("/")[4]

                if (breed.contains("-")) {
                    breed = breed.replace("-", " ")
                }
            }

            singleDog.breed = breed.capitalize()
            singleDog.shortText = getShortText()

            dogsList.add(singleDog)
        }
        return dogsList
    }

    private fun getShortText(): String {

        val dogSynonyms = mutableListOf(
            "pup",
            "puppy",
            "doggy",
            "cur",
            "hound",
            "mutt",
            "pooch",
            "stray",
            "tyke",
            "bowwow",
            "tail-wagger",
            "fido",
            "mongrel",
            "doggo"
        )
        val adjectives = mutableListOf(
            "nice",
            "good",
            "lovely",
            "pretty",
            "cute",
            "fine",
            "sweet",
            "good lookin",
            "handsome",
            "great"
        )

        val randomDogSynonym = Random.nextInt(0, 14)
        val randomAdjective = Random.nextInt(0, 10)

        return "A" + " " + adjectives[randomAdjective] + " " + dogSynonyms[randomDogSynonym]

    }
}