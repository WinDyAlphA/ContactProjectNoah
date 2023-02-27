package com.contactproject

import android.os.Bundle
import com.contactproject.databinding.ActivityShowBinding


import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
@Suppress("DEPRECATION")
class ShowContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowBinding
    private var modif = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val contact = intent.getSerializableExtra("contact") as Person

        val photo = photoActions.loadPhotoFromExternalStorage(contact.imageuri)
        if (photo != null) {
            binding.imageProfilePic.setImageBitmap(photo)
        }



        binding.buttonModifier.setOnClickListener {
            if (!modif) {
                //start animation
                //val animation = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
                //binding.imageProfilePic.startAnimation(animation)

                modif = true
                //lancer un intent
                val intent = Intent(this, ModifierContactActivity::class.java)

                intent.putExtra("contact", contact)
                startActivity(intent)
            } else {
                //stop
                binding.imageProfilePic.clearAnimation()
                modif = false
            }
        }


        binding.annulerShowContact.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("contact", contact)
            startActivity(intent)
        }
        binding.buttonAppeler.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + binding.textViewNumeroContact.text.toString())
            startActivity(intent)
        }
        binding.buttonMessage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:" + binding.textViewNumeroContact.text.toString())
            startActivity(intent)
        }
        binding.buttonInfo.setOnClickListener {
            val intent = Intent(this, ShowInfoContactActivity::class.java)
            intent.putExtra("contact", contact)
            startActivity(intent)
        }

        binding.textViewNomContact.text = contact.firstName
        binding.textViewPrenomContact.text = contact.lastName
        binding.textViewNumeroContact.text = contact.num

    }


}

