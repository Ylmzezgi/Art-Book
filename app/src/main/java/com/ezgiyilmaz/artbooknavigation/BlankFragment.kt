package com.ezgiyilmaz.artbooknavigation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BlankFragment : Fragment() {
    private lateinit var artList :ArrayList<Art>
    private lateinit var artAdapter: ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // Menü işlemleri için gerekli
        artList=ArrayList()


        try {
            val database=requireContext().openOrCreateDatabase("Arts", AppCompatActivity.MODE_PRIVATE,null)

            val cursor=database.rawQuery("SELECT * FROM arts",null)
            val artNameIx=cursor.getColumnIndex("artname")
            val idIx=cursor.getColumnIndex("id")


            //bunlar hangi kolonlarda kayıtlı alabiliriz artık ve bunu moveToNext ile
            while (cursor.moveToNext()) { //imlecimiz ilerlediği sürece bu işlemi yap
                val name=cursor.getString(artNameIx)
                val id=cursor.getInt(idIx)

                val art =Art(name,id) // şimdi bunu ArrayListe koyup recyclerwiede gösterelim,
                artList.add(art)
            }
            cursor.close()
            artAdapter.notifyDataSetChanged() // artAdapterı haber gönder bunları bu verileri göstersin internetten veri çekerken kullanılması gereken bir özellik

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        // Toolbar'ı ayarla
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context) // LayoutManager'i ayarla

        artAdapter=ArtAdapter(artList)
        recyclerView.adapter = artAdapter

        return view
    }
}
