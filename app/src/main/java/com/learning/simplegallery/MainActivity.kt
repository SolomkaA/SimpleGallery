package com.learning.simplegallery

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val STORAGE_PERMISSION_CODE = 101
private const val ACCESS_MEDIA_PERMISSION_CODE = 102


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        checkPermission(Manifest.permission.ACCESS_MEDIA_LOCATION, ACCESS_MEDIA_PERMISSION_CODE)

    }

    override fun onResume() {
        super.onResume()

        val gridView : GridView = findViewById(R.id.galleryGridView)

        val imageList = mutableListOf<Image>()

        val projection = arrayOf(MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE)

        val selection = "${MediaStore.Images.Media.SIZE} >= ?"

        val selectionArgs = arrayOf("1")

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        applicationContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given images.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                )
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                imageList += Image(contentUri, name, size)

            }
        }

        gridView.adapter = GridAdapter(this, imageList)

        gridView.setOnItemClickListener { parent, view, i, l ->
            val intent = Intent(this@MainActivity, FullImage::class.java)
            intent.putExtra("id", imageList[i].uri)
            startActivity(intent)
        }
    }
    data class Image(val uri: Uri,
                     val name: String,
                     val size: Int
    )

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }
}

