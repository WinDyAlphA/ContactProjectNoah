package com.contactproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.contactproject.databinding.ActivityShowInfoBinding
@Suppress("DEPRECATION")
class ShowInfoContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowInfoBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val contact = intent.getSerializableExtra("contact") as Person

        binding.annulerShowContact.setOnClickListener {
            val intent = Intent(this, ShowContactActivity::class.java)
            intent.putExtra("contact", contact)
            startActivity(intent)
        }




        binding.textViewPrenomContact.text = contact.lastName

        if(contact.firstName != "null" && contact.firstName != ""){
            binding.textViewNomContact.text = contact.firstName
        }else{
            binding.textViewNomContact.text = "Pas de Nom défini"
        }
        binding.textViewNumContact.text = contact.num
        if(contact.email != "mail" && contact.email != "null"){
            binding.textViewEmailContact.text = contact.email
        }else{
            binding.textViewEmailContact.text = "Pas d'email défini"
        }
        if(contact.genre != GENRE.Default){
            binding.textViewGenreContact.text = contact.genre.toString()
        }else{
            binding.textViewGenreContact.text = "Pas de genre défini"
        }
        if(contact.date != "null"){
            binding.textViewDateContact.text = contact.date
        }else{
            binding.textViewDateContact.text = "Pas de date de naissance définie"
        }




    }
}