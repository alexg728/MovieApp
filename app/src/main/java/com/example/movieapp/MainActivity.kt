package com.example.movieapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.ReviewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<MovieData>()
    private lateinit var adapter: MovieAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var videoView: VideoView
    private lateinit var textView: TextView
    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleView)
        searchView = findViewById(R.id.searchView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        videoView = findViewById(R.id.videoView)
        textView = findViewById(R.id.textView)
        welcomeText = findViewById(R.id.welcome_text)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addDataToList()

        adapter = MovieAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    searchView.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    textView.visibility = View.GONE
                    videoView.visibility = View.GONE
                    welcomeText.visibility = View.VISIBLE
                }
                R.id.reviews -> {
                    searchView.visibility = View.VISIBLE
                    recyclerView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    videoView.visibility = View.GONE
                    welcomeText.visibility = View.GONE
                }
                R.id.upcoming -> {
                    searchView.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    welcomeText.visibility = View.GONE
                    showUpcomingMovie()
                }
                else -> {

                }
            }
            true
        }
    }


    private fun showUpcomingMovie() {

        videoView.visibility = View.VISIBLE
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.barbie_video))
        videoView.start()

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        textView.visibility = View.VISIBLE
        textView.text = "To live in Barbie Land is to be a perfect being in a perfect place. Unless you have a full-on existential crisis. Or you're a Ken.\n" +
                "Director: Greta Gerwig\n" +
                "Writers: Greta Gerwig, Noah Baumbach\n" +
                "Stars: Margot Robbie, Hari Nef, Ryan Gosling"
    }


    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<MovieData>()
            for (movie in mList) {
                if (movie.title.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                    filteredList.add(movie)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {
        mList.add(MovieData("Lord of the Rings: Fellowship of the Ring", R.drawable.lotr))
        mList.add(MovieData("The Godfather", R.drawable.godfather))
        mList.add(MovieData("The Matrix", R.drawable.matrix))
        mList.add(MovieData("Star Wars: Attack of the Clones", R.drawable.sw))
        mList.add(MovieData("Forrest Gump", R.drawable.gump))
    }

    inner class MovieAdapter(var mList: List<MovieData>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

        inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val logo: ImageView = itemView.findViewById(R.id.logoIv)
            val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        }

        fun setFilteredList(mList: List<MovieData>) {
            this.mList = mList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

            holder.logo.setImageResource(mList[position].logo)
            holder.titleTv.text = mList[position].title

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ReviewActivity::class.java)
                intent.putExtra("position", position)
                holder.itemView.context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return mList.size
        }
    }

}

class MovieData(val title: String, val logo: Int)