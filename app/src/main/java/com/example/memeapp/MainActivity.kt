package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageUrl:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private  fun loadMeme(){
        val progressBar:ProgressBar=findViewById(R.id.progressBar)
        progressBar.visibility= VISIBLE

        var memeImageView: ImageView =findViewById(R.id.memeImageView)

        val url= "https://meme-api.herokuapp.com/gimme"


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->

                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                       progressBar.visibility=View.GONE
                        return false
                    }

                }).into(memeImageView)

            },
            {
                Toast.makeText(this,"Some thing went wrong", Toast.LENGTH_LONG).show();
                Log.d("error",it.localizedMessage)  })


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this cool meme I got from Reddit $currentImageUrl")
        intent.type="text/plain"
        val chooser=Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
}