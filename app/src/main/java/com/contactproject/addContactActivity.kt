package com.contactproject

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.contactproject.databinding.ActivityAddBinding

class addContactActivity : AppCompatActivity() {
    private val pickImage = 100
    private val pickPhoto = 200
    private var imageUri: Uri? = null
    private var customPhoto = false
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.annulerAddContact.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.enregistrerAddContact.setOnClickListener {
            if(binding.TextEditNom.text.toString() != "" && binding.TextEditPrenom.text.toString() != "" && binding.TextEditNum.text.toString() != ""){
                val n1 = Person(binding.TextEditNom.text.toString(), binding.TextEditPrenom.text.toString(), "test", binding.TextEditNum.text.toString(),"mail" , GENRE.Homme)
                val intent = Intent()
                intent.putExtra("contact", n1)
                setResult(RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this, "non", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageProfilePic.setOnClickListener {
            //demander permissions pour la camera
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
            }
            PopupMenu(this, binding.imageProfilePic).apply {
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_selectionner -> {
                            val gallery = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI
                            )
                            startActivityForResult(gallery, pickImage)
                            true
                        }
                        R.id.action_prendre -> {
                            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(camera, pickPhoto)
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.popup_menu)
                show()
            }
        }



    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.imageProfilePic.setImageURI(imageUri)
            customPhoto = true
        }
        if (resultCode == RESULT_OK && requestCode == pickPhoto) {
            val image = data?.extras?.get("data")
            binding.imageProfilePic.setImageBitmap(image as android.graphics.Bitmap)
            customPhoto = true
        }
    }
}