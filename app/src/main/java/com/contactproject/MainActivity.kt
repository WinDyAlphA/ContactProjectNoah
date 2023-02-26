package com.contactproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.contactproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val NEW_ACTIVITY_REQUEST_CODE = 1
    private val contacts = ArrayList<Person>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val n1 = Person("Jean", "Dupont", "01/01/2000", "06 00 00 00 00", "mail" , GENRE.Homme)
        val n2 = Person("Jeanne", "Dupont", "01/01/2000", "06 00 00 00 00", "mail" , GENRE.Femme)
        val n3 = Person("Jean", "Dupont", "01/01/2000", "06 00 00 00 00", "mail" , GENRE.Autre)
        //ajouter les personnes dans la liste

        contacts.add(n1)
        contacts.add(n2)
        contacts.add(n3)
        //mettre cette liste dans la list view
        val myAdapter = PersonAdapter(this, contacts)
        binding.listContacts.adapter = myAdapter
        //set on click listener sur chaque item de la list view
        binding.listContacts.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ShowContactActivity::class.java)
            intent.putExtra("contact", contacts[position])
            startActivity(intent)
        }
        binding.addContact.setOnClickListener {
            val intent = Intent(this, addContactActivity::class.java)
            startActivityForResult(intent, NEW_ACTIVITY_REQUEST_CODE)

        }
        val intent = intent

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Vérifier que la demande provient de l'activité NewActivity et que les données sont disponibles
        if (requestCode == NEW_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val contact = data.getSerializableExtra("contact") as Person
            contacts.add(contact)
        }
    }
}