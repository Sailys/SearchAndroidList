package com.searchandroidlist.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import com.searchandroidlist.BuildConfig
import com.searchandroidlist.R
import java.io.*

class FileUtils {

    companion object {

        var dir = File(Environment.getExternalStorageDirectory(), ".survey")

        fun saveToStorage(
            context: Context,
            filePath: String
        ): String {
            val sourceFile = File(filePath)
            return sourceFile.absolutePath
        }

        private fun saveBitmapToInternalStorage(
            context: Context,
            filename: String,
            bitmap: Bitmap
        ) {
            try {
                context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream)) {
                        throw IOException("Unable to save bitmap")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getImageFromStorage(context: Context, imageFileName: String): String {
            val dirApp =
                context.getExternalFilesDir(context.getString(R.string.folder_name)).toString()
            val directory = File(dirApp)
            return if (directory.exists()) {
                val file = File(directory, imageFileName)
                if (file.exists()) file.absolutePath else ""
            } else ""
        }

        fun deleteImageFromStorage(context: Context, imageFileName: String): Boolean {
            return if (dir.exists()) {
                val file = File(dir, imageFileName)
                file.delete()
            } else false
        }

        @Throws(IOException::class)
        fun createImageFile(context: Context): File {
            // Create an image file name
//            val dir =
//                context.getExternalFilesDir(context.getString(R.string.folder_name)).toString()
            val timeStamp: String = System.currentTimeMillis().toString()
            val imageFileName = "survey_$timeStamp"
//            val storageDir =
//                File(dir)
            return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                dir      /* directory */
            )

//            // Save a file: path for use with ACTION_VIEW intents
//            mCurrentPhotoPath = image.absolutePath
        }

        @Throws(IOException::class)
        fun saveImage(bitmap: Bitmap, name: String, context: Context) {
            val saved: Boolean
            val fos: OutputStream?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES.toString()
                )
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir =
                    context.getExternalFilesDir(context.getString(R.string.folder_name)).toString()
                val file = File(imagesDir)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(imagesDir, "$name.png")
                fos = FileOutputStream(image)
            }
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
            Log.e("is file save ", "is file save $saved")
            fos?.flush()
            fos?.close()

        }

        fun saveImage(bitmap: Bitmap, context: Context): String {
//            val dir =
//                context.getExternalFilesDir(context.getString(R.string.folder_name)).toString()
//            val myDir = File(dir)
//            myDir.mkdirs()
//            val timeStamp: String = System.currentTimeMillis().toString()
//            val fName = "survey_$timeStamp.jpg"
//            val file = File(myDir, fName)
//            if (file.exists()) file.delete()
//            try {
//                val out = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//                out.flush()
//                out.close()
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    createDir()
                }
            }
            //create a file to write bitmap data
            var file: File? = null
            val timeStamp: String = System.currentTimeMillis().toString()
            val fName = "survey_$timeStamp.jpg"
            try {
                file = File(dir.toString() + File.separator + fName)
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                return "" // it will return null
            }
        }

        /* Checks if external storage is available for read and write */
        fun isExternalStorageWritable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

//        fun getServerImageUrl(imageName: String): String {
//            val url = BuildConfig.IMAGE_URL + "survey/" + imageName
//            //Log.e("is file save ", "is file save $url")
//            return url
//        }

        fun askForPermissions(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    context.startActivity(intent)
                    return false
                }
                createDir()
                return true
            } else
                return true
        }

        private fun createDir() {
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }

        fun checkFileExist(filePath: String): Boolean {
            if (dir.exists()) {
                val file = File(dir, filePath)
                if (file.exists()) {
                    return true
                }
            }
            return false
        }

        fun deleteDirectory() {
            if (dir.exists() && dir.listFiles()?.isNotEmpty() == true) {
                for (file in dir.listFiles()) {
                    if (file.isDirectory) {
                        deleteDirectory()
                    } else {
                        file.delete()
                    }
                }
            }
        }
    }
}