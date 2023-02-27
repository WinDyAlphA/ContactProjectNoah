package com.contactproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
@Suppress("DEPRECATION")
class photoActions {
    companion object {


        fun loadPhotoFromExternalStorage(photoPath: String): Bitmap? {
            val photoFile = File(photoPath)
            if (!photoFile.exists()) {
                // Le fichier photo n'existe pas
                return null
            }

            try {
                val inputStream = FileInputStream(photoFile)
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.RGB_565
                val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                inputStream.close()
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
        fun savePhotoToExternalStorage(bitmap: Bitmap): String {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                // La mémoire externe n'est pas disponible
                return "null"
            }

            val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val date = System.currentTimeMillis()
            val photoFile = File(picturesDirectory, "photo$date.jpg")

            try {
                val fileOutputStream = FileOutputStream(photoFile)
                val bufferedOutputStream = BufferedOutputStream(fileOutputStream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream)
                bufferedOutputStream.flush()
                bufferedOutputStream.close()
                fileOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return "null"
            }

            return photoFile.absolutePath
        }
    }


}