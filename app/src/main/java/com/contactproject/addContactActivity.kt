package com.contactproject

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.contactproject.databinding.ActivityAddBinding

import java.util.*
@Suppress("DEPRECATION")
class addContactActivity : AppCompatActivity() {
    private val pickImage = 100
    private val pickPhoto = 200
    private var imageUri: Uri? = null
    private var customPhoto = false
    private var bitmap: String = "null"
    private var date: String = "null"
    private var sexe: GENRE = GENRE.Default
    private var email: String = "null"
    private var newChamp: Int = 0
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
            if( binding.TextEditPrenom.text.toString() != "" && binding.TextEditNum.text.toString() != ""){
                //compter nbre de contact dans sql
                val db = ContactDatabaseHelper(this).writableDatabase
                val countQuery = "SELECT * FROM contacts"
                val cursor = db.rawQuery(countQuery, null)
                val count = cursor.count
                cursor.close()


                val n1: Person = if (!customPhoto) {
                    Person(count+1,binding.TextEditNom.text.toString(), binding.TextEditPrenom.text.toString(), date, binding.TextEditNum.text.toString(),email , sexe, "null"   )
                } else {
                    Person(count+1,binding.TextEditNom.text.toString(), binding.TextEditPrenom.text.toString(), date, binding.TextEditNum.text.toString(),email , sexe, bitmap)
                }
                val intent = Intent()
                intent.putExtra("contact", n1)
                setResult(RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this, "Veuillez remplir les champ nécessaires", Toast.LENGTH_SHORT).show()
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
        binding.buttonAddChamp.setOnClickListener(){
            PopupMenu(this, binding.buttonAddChamp).apply {
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_date -> {
                            //demande sa date de naissance avec un date picker
                            DatePickerDialog(
                                this@addContactActivity,
                                { view, year, month, dayOfMonth ->
                                    val dateStrAffiche = "$dayOfMonth/${month+1}/$year"
                                    val dateStr = "$dayOfMonth/${month}/$year"
                                    if (newChamp == 1){
                                        binding.textViewAddChamp2.text = dateStr
                                        binding.textViewAddChamp2.visibility = View.VISIBLE
                                    }else{
                                        binding.textViewAddChamp1.text = dateStr
                                        binding.textViewAddChamp1.visibility = View.VISIBLE
                                    }
                                    newChamp += 1

                                },
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                            ).show()

                            true
                        }
                        R.id.action_email -> {

                            AddEmailDialog().show(
                                supportFragmentManager, "AddEmailDialog")

                            true
                        }
                        R.id.action_sexe -> {
                            PopupMenu(this@addContactActivity, binding.buttonAddChamp).apply {
                                setOnMenuItemClickListener { item ->
                                    when (item.itemId) {
                                        R.id.action_homme -> {
                                            sexe = GENRE.Homme
                                            Log.d("sexe", sexe.toString())

                                            if (newChamp == 1){
                                                binding.textViewAddChamp2.text = sexe.toString()
                                                binding.textViewAddChamp2.visibility = View.VISIBLE
                                            }else{
                                                binding.textViewAddChamp1.text = sexe.toString()
                                                binding.textViewAddChamp1.visibility = View.VISIBLE
                                            }
                                            newChamp += 1
                                            true
                                        }
                                        R.id.action_femme -> {

                                            sexe = GENRE.Femme
                                            if (newChamp == 1){
                                                binding.textViewAddChamp2.text = sexe.toString()
                                                binding.textViewAddChamp2.visibility = View.VISIBLE
                                            }else{
                                                binding.textViewAddChamp1.text = sexe.toString()
                                                binding.textViewAddChamp1.visibility = View.VISIBLE
                                            }
                                            newChamp += 1
                                            true
                                        }
                                        R.id.action_autre -> {
                                            sexe = GENRE.Autre
                                            if (newChamp == 1){
                                                binding.textViewAddChamp2.text = sexe.toString()
                                                binding.textViewAddChamp2.visibility = View.VISIBLE
                                            }else{
                                                binding.textViewAddChamp1.text = sexe.toString()
                                                binding.textViewAddChamp1.visibility = View.VISIBLE
                                            }
                                            newChamp += 1
                                            true
                                        }
                                        else -> false
                                    }
                                }
                                inflate(R.menu.sexe_menu)
                                show()
                            }
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.champ_menu)
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
            val image = data?.extras?.get("data") as Bitmap
            binding.imageProfilePic.setImageBitmap(image)
            bitmap = photoActions.savePhotoToExternalStorage(image)
            customPhoto = true
        }


    }

    fun addEmail(mail: String) {
        email = mail
    }


}