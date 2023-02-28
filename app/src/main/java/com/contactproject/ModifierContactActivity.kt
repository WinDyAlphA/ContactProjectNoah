package com.contactproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.contactproject.databinding.ActivityModifBinding
import java.util.*


@Suppress("DEPRECATION")
class ModifierContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifBinding
    private var bitmap: String = "null"
    private val pickImage = 100
    private var genre: GENRE = GENRE.Default
    private var imageUri: Uri? = null
    private var customPhoto = false
    private val pickPhoto = 200
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val contact = intent.getSerializableExtra("contact") as Person

        val oldContact = Person(contact.id,contact.firstName, contact.lastName, contact.date, contact.num, contact.email, contact.genre, contact.imageuri)
        val photo = photoActions.loadPhotoFromExternalStorage(contact.imageuri)
        if (photo != null) {
            binding.imageProfilePic.setImageBitmap(photo)
        }
        if (contact.firstName  != "null"){
            binding.textViewNomContact.setText(contact.firstName)
        }else{
            binding.textViewNomContact.setText("")
            binding.textViewNomContact.hint = "Nom"
        }


        binding.textViewPrenomContact.setText(contact.lastName)
        binding.textViewNumContact.setText(contact.num)
        if (contact.email  != "null" && contact.email != "mail" && contact.email != "email"){
            binding.textViewEmailContact.setText(contact.email)
        }else{
            binding.textViewEmailContact.setText("")
            binding.textViewEmailContact.hint = "email"
        }
        if (contact.date  != "null"){
            binding.textViewDateContact.text = contact.date
        }else{
            binding.textViewDateContact.text = "date de naissance"
        }
        if (contact.genre != GENRE.Default) {
            binding.buttonSexe.text = contact.genre.toString()
        }else{
            binding.buttonSexe.text = "Sexe"
        }















        binding.imageProfilePic.setOnClickListener(){
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

        binding.buttonSexe.setOnClickListener(){
            val popup = PopupMenu(this, binding.buttonSexe)
            popup.menuInflater.inflate(R.menu.sexe_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_homme -> {
                        binding.buttonSexe.text = "Homme"
                        genre = GENRE.Homme
                        true
                    }
                    R.id.action_femme -> {
                        binding.buttonSexe.text = "Femme"
                        genre = GENRE.Femme
                        true
                    }
                    R.id.action_autre -> {
                        binding.buttonSexe.text = "Autre"
                        genre = GENRE.Autre
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.textViewDateContact.setOnClickListener(){
            val year: Int
            val month: Int
            val day: Int
            if (contact.date != "null" && contact.date != "test" && contact.date != "") {
                val date = contact.date.split("/")
                day = date[0].toInt()
                month = date[1].toInt()
                year = date[2].toInt()
            }else{
                year = Calendar.getInstance().get(Calendar.YEAR)
                month = Calendar.getInstance().get(Calendar.MONTH)
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            }
            DatePickerDialog(
                this@ModifierContactActivity,
                { _, yearStr, monthStr, dayOfMonth ->
                    val dateStrAffiche = "$dayOfMonth/${monthStr+1}/$yearStr"
                    val dateStr = "$dayOfMonth/${monthStr}/$yearStr"
                    binding.textViewDateContact.text = dateStrAffiche
                    contact.date = dateStr
                },
                year,month,day
            ).show()
        }

        binding.enregistrerInfoContact.setOnClickListener(){
            contact.firstName = binding.textViewPrenomContact.text.toString()
            contact.lastName = binding.textViewNomContact.text.toString()
            contact.num = binding.textViewNumContact.text.toString()
            contact.email = binding.textViewEmailContact.text.toString()
            contact.date = binding.textViewDateContact.text.toString()
            if(customPhoto){
                contact.imageuri = bitmap
            }
            if(genre != GENRE.Default){
                contact.genre = genre
            }

            //create a clone of contact but serialized
            val database = ContactDatabaseHelper(this).writableDatabase

            val contentValues = ContentValues().apply{
                put("first_name", contact.firstName)
                put("last_name", contact.lastName)
                put("phone_number", contact.num)
                put("email", contact.email)
                put("gender", contact.genre.toString())
                put("date", contact.date)
                put("image", contact.imageuri)
            }
            //update the contact in the database
            database.update("contacts", contentValues, "_id = ?", arrayOf(contact.id.toString()))
            database.close()

            val intent = Intent(this, ShowContactActivity::class.java)

            intent.putExtra("contact", contact)
            startActivity(intent)
        }

        binding.annulerShowContact.setOnClickListener {
            val intent = Intent(this, ShowContactActivity::class.java)
            intent.putExtra("contact", oldContact)
            startActivity(intent)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {

            imageUri = data?.data
            //imageUri to bitmap
            val image = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            binding.imageProfilePic.setImageBitmap(image)
            bitmap = photoActions.savePhotoToExternalStorage(image)
            customPhoto = true
        }
        if (resultCode == RESULT_OK && requestCode == pickPhoto) {
            val image = data?.extras?.get("data") as Bitmap
            binding.imageProfilePic.setImageBitmap(image)
            bitmap = photoActions.savePhotoToExternalStorage(image)
            customPhoto = true
        }


    }

}