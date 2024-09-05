package com.ezgiyilmaz.artbooknavigation
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ezgiyilmaz.artbooknavigation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding= ActivityMainBinding.inflate(layoutInflater)
            var view=binding.root
            setContentView(view)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInfalter=menuInflater
        menuInfalter.inflate(R.menu.art_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    // Menü öğelerine tıklanma işlemleri
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.art_menu) {
            val action = BlankFragmentDirections.actionBlankFragmentToDetailFragment()
            Navigation.findNavController(this, R.id.fragment).navigate(action)

        }
        return super.onOptionsItemSelected(item)
    }

    }