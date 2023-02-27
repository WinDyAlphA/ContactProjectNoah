package com.contactproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.contactproject.APIClient.client
import com.contactproject.databinding.ActivityMainBinding
import com.contactproject.pojo.MultipleResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var database: SQLiteDatabase
    private lateinit var binding: ActivityMainBinding
    private val NewActivityRequestCode = 1
    private val contacts = ArrayList<Person>()
    var apiInterface: APIInterface? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //on cache la barre d'action

        val call: Call<MultipleResource?>? = client!!.create(APIInterface::class.java).doGetListResources()
        if (call != null) {
            call.enqueue(object : Callback<MultipleResource?> {
                override fun onResponse(call: Call<MultipleResource?>?, response: Response<MultipleResource?>) {
                    Log.d("TAG", response.code().toString() + "")
                    var displayResponse = ""
                    val resource: MultipleResource? = response.body()
                    val res = resource?.results
                    //loop in results
                    for (i in res!!.indices) {
                        val text = res[i].name
                        val url = res[i].url
                        displayResponse += """${text.toString()} ${url.toString()}
    """
                        Log.d("TAG", response.code().toString() + "")
                        Log.d("TAG", "onResponse: $displayResponse")
                    }
                    //displayResponse += """${text.toString()} ${url.toString()}
    //"""

                }

                override fun onFailure(call: Call<MultipleResource?>, t: Throwable?) {
                    call.cancel()
                }
            })
        }

        binding.seekBar.max = 25
        binding.seekBar.progress = 25
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val alphabet = "zyxwvutsrqponmlkjihgfedcba".toCharArray()

                //descendre dans la listeview jusqu'à la lettre c'est bcp trop stylé sah
                val listContactSorted = contacts.sortedBy { it.lastName }
                var i = 0

                while (i < listContactSorted.size) {
                    if (listContactSorted[i].lastName[0] == alphabet[progress]) {
                        binding.listContacts.setSelection(i)
                        break
                    }
                    i++
                }
                //afficher la lettre en haut de la listeview
                binding.letter.text = alphabet[progress].toString().toUpperCase(Locale.ROOT)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        //on crée la base de données avec la classe ContactDatabaseHelper
        database = ContactDatabaseHelper(this).writableDatabase
        //on crée un curseur pour parcourir la base de données
        val cursor = database.query("contacts", null, null, null, null, null, "LOWER(last_name)"+" ASC")
        //val cursor2 = database.query("contacts", null, null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            /*val id = cursor.getInt(cursor.getColumnIndex("_id"))
            val firstName = cursor.getString(cursor.getColumnIndex("first_name"))
            val lastName = cursor.getString(cursor.getColumnIndex("last_name"))
            val phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val genre = cursor.getString(cursor.getColumnIndex("gender"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val image = cursor.getString(cursor.getColumnIndex("image"))
            val contact = Person(id,firstName, lastName, date, phoneNumber, email, GENRE.valueOf(genre), image)*/

            //One line version
            //On crée un objet Person avec les données de la base de données
            val contact = Person(cursor.getInt(cursor.getColumnIndex("_id")),cursor.getString(cursor.getColumnIndex("first_name")), cursor.getString(cursor.getColumnIndex("last_name")), cursor.getString(cursor.getColumnIndex("date")), cursor.getString(cursor.getColumnIndex("phone_number")), cursor.getString(cursor.getColumnIndex("email")), GENRE.valueOf(cursor.getString(cursor.getColumnIndex("gender"))), cursor.getString(cursor.getColumnIndex("image")))




            //si il y a personne dans la base de données
            contacts.add(contact)

            cursor.moveToNext()
        }
        if (contacts.isEmpty()) {
            //faire apparaitre un TextView qui dit qu'il n'y a pas de contact
            binding.noContact.visibility = View.VISIBLE
        }else{
            binding.noContact.visibility = View.INVISIBLE
        }



        cursor.close()
        //mettre cette liste dans la list view
        val myAdapter = PersonAdapter(this, contacts)
        binding.listContacts.adapter = myAdapter

        var filteredList: List<Person>? = null

        binding.rechercherContact.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            filteredList = contacts.filter { it.firstName.contains(text.toString(), true) || it.lastName.contains(text.toString(), true) }
            val myAdapterPerson = PersonAdapter(this, filteredList as ArrayList<Person>)
            binding.listContacts.adapter = myAdapterPerson
        })

        //set on click listener sur chaque item de la list view
        binding.listContacts.setOnItemLongClickListener { _, _, position, _ ->
            //popup pour supprimer le contact
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Supprimer le contact")
            builder.setMessage("Voulez-vous vraiment supprimer ce contact ?")
            builder.setPositiveButton("Oui") { _, _ ->
                //supprimer le contact de la base de données
                val selectionId = "_id LIKE ?"
                val selectionArgs = if (filteredList != null) {
                     arrayOf(filteredList!![position].id.toString())
                } else {
                    arrayOf(contacts[position].id.toString())
                }
                database.delete("contacts", selectionId, selectionArgs)
                //supprimer le contact de la liste
                Log.d("MainActivity", "onCreate: $filteredList[$position]")
                //remove contact form contact where id = filteredList[position].id
                if (filteredList != null) {
                    contacts.remove(filteredList!![position])
                    myAdapter.notifyDataSetChanged()
                } else {
                    contacts.remove(contacts[position])
                    myAdapter.notifyDataSetChanged()
                }
                //concatener deux string
                filteredList = contacts.filter { it.firstName.contains(binding.rechercherContact.text, true) || it.lastName.contains(binding.rechercherContact.text , true) }
                val myAdapterPerson = PersonAdapter(this, filteredList as ArrayList<Person>)
                binding.listContacts.adapter = myAdapterPerson
                //mettre à jour la list view


                if (contacts.isEmpty()) {
                    //faire apparaitre un TextView qui dit qu'il n'y a pas de contact
                    binding.noContact.visibility = View.VISIBLE
                }else{
                    binding.noContact.visibility = View.INVISIBLE
                }
            }
            builder.setNegativeButton("Non") { _, _ ->
                //do nothing
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }


        binding.listContacts.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ShowContactActivity::class.java)
            if (filteredList != null) {
                intent.putExtra("contact", filteredList!![position])
            } else {
                intent.putExtra("contact", contacts[position])
            }
            startActivity(intent)
        }
        binding.addContact.setOnClickListener {
            val intent = Intent(this, addContactActivity::class.java)
            startActivityForResult(intent, NewActivityRequestCode)

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Vérifier que la demande provient de l'activité NewActivity et que les données sont disponibles
        if (requestCode == NewActivityRequestCode && resultCode == RESULT_OK && data != null) {
            val contact = data.getSerializableExtra("contact") as Person

            contacts.add(contact)
            val contentValues = ContentValues().apply{
                put("first_name", contact.firstName)
                put("last_name", contact.lastName)
                put("phone_number", contact.num)
                put("email", contact.email)
                put("gender", contact.genre.toString())
                put("date", contact.date)
                put("image", contact.imageuri)
            }
            //on insert les contentValues dans la base de données
            database.insert("contacts", null, contentValues)
            val myAdapterPerson = PersonAdapter(this, contacts)
            binding.listContacts.adapter = myAdapterPerson
            //message si la bd est vide invisible
            binding.noContact.visibility = View.INVISIBLE

        }
    }
}


/*private fun SQLiteDatabase.clearDatabase() {
    delete("contacts", null, null)

}*/
