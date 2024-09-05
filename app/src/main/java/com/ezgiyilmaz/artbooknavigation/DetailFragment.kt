package com.ezgiyilmaz.artbooknavigation

import android.Manifest
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException

class DetailFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String> // izinler string olduğu için
    private lateinit var intentToGallery: Intent
    var selectedBitmap: Bitmap? = null
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button=view.findViewById(R.id.button)

        imageView = view.findViewById(R.id.imageView)

        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give permission") {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }.show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                }
            } else {
                // izin verildiyse galeriye gidip fotoğraf seç
                intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }

        button.setOnClickListener{
            val artName=view.findViewById<EditText>(R.id.artEditText).toString()
            val artistName=view.findViewById<EditText>(R.id.artistEditText).toString()
            val yearText=view.findViewById<EditText>(R.id.yearEditText).toString()

            if(selectedBitmap!=null){
                val smallBitmap=makeSmallerBitmap(selectedBitmap!!,300)
                val outputStream= ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
                val byteArray=outputStream.toByteArray()

                try {
                    val database =requireContext().openOrCreateDatabase("Arts", MODE_PRIVATE,null)
                    database.execSQL("CREATE TABLE İF NOT EXİSTS arts (id INTEGER PRİMARY KEY, artname VARCHAR, artistname VARCHAR, year VARCHAR, image BLOB)")
                    //aldığımız değişkenleri şimdi buraya kaydedeceğiz

                    val sqlString="INSERT INTO arts (artname, artistname, year, image) VALUES (?,?,?,?)"
                    val statement=database.compileStatement(sqlString)
                    statement.bindString(1,artName)
                    statement.bindString(2,artistName)
                    statement.bindString(3,yearText)
                    statement.bindBlob(4,byteArray)
                    statement.execute() // bunları uygula



                }catch (e:Exception){
                    e.printStackTrace()
                }
                val intent=Intent(requireContext(),MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }
    }

    private fun makeSmallerBitmap(image:Bitmap,maximumSize:Int):Bitmap{
        var width=image.width
        var height=image.height

        val bitmapRatio:Double=width.toDouble()/height.toDouble()
        if(bitmapRatio>1){
            width=maximumSize
            val scaledHeight=width/bitmapRatio
            height=scaledHeight.toInt()

        }else{
            height=maximumSize
            val scaledWidh=height*bitmapRatio
            width=scaledWidh.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }


    private fun registerLauncher() {

            activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
                if(result.resultCode== RESULT_OK){
                    val intentFromResult=result.data
                    if(intentFromResult!=null){
                        val imageData=intentFromResult.data
                        if (imageData!=null){
                            try {
                                imageView.setImageURI(imageData)
                                if (Build.VERSION.SDK_INT >= 28) {
                                    //bitmape çevirme
                                    val source = ImageDecoder.createSource(requireContext().contentResolver,imageData)
                                    selectedBitmap=ImageDecoder.decodeBitmap(source)
                                    imageView.setImageBitmap(selectedBitmap)
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
           if (result){
               val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
               activityResultLauncher.launch(intentToGallery )
           } else {
               Toast.makeText(requireContext(),"Permission needed",Toast.LENGTH_LONG).show()
           }
        }
    }
}