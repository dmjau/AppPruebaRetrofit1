package com.anuar81.PruebaRetroFit1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val URL_BASE = "https://dog.ceo/api/breed/"

    lateinit var imagesPuppies: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //nosotros somos los escuchadores del query
        main_search_breed.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchByName(query.toLowerCase(Locale.getDefault()))
        }
        return true
    }

    //No lo usamos porque mira el cambio de texto cada vez
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showErrorToast() {
        Toast.makeText(this, getString(R.string.main_error_msg), Toast.LENGTH_SHORT).show()
    }

    //Creamos la funcion que nos devuelve un objeto Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_BASE) //agregamos la url base que definimos en una constante
            .addConverterFactory(GsonConverterFactory.create()) // esto convierte y entiende el json viene de GSON
            .build()
    }

    //Corrmos la coRutina para el llamado en Background
    private fun searchByName(query: String) = GlobalScope.launch {
        val call = getRetrofit().create(ApiService::class.java).getDogByBreds("$query/images").execute()
        val response = call.body() as DogsResponse

        launch(Dispatchers.Main) {
            if (response.status == "success") {
                initRecycler(response.images)
            } else {
                showErrorToast()
            }
        }

    }

    //trabajamos con el recycler
    private fun initRecycler(images: List<String>){
        if(images.isNotEmpty()){
            imagesPuppies = images
        }
        main_recycler.layoutManager = LinearLayoutManager(this)
        main_recycler.adapter = RecyclerDogAdapter(imagesPuppies)
    }
}