package com.contactproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
@Suppress("DEPRECATION")
class ContactDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "contact_database"

        // Table Names
        private const val TABLE_CONTACTS = "contacts"

        // Contacts Table Columns
        private const val KEY_ID = "_id"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_EMAIL = "email"
        private const val KEY_GENDER = "gender"
        private const val KEY_DATE = "date"
        private const val KEY_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE $TABLE_CONTACTS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_FIRST_NAME TEXT NOT NULL,"
                + "$KEY_LAST_NAME TEXT,"
                + "$KEY_PHONE_NUMBER TEXT NOT NULL,"
                + "$KEY_EMAIL TEXT,"
                + "$KEY_GENDER TEXT,"
                + "$KEY_DATE DATE,"
                + "$KEY_IMAGE BLOB"
                + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }




}