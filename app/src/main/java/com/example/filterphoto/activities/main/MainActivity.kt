package com.example.filterphoto.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.filterphoto.R
import com.example.filterphoto.activities.edit.EditImgActivity
import com.example.filterphoto.activities.loginAuth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    public companion object
    {
        private const val TAG ="MainActivity"
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "image_Uri"
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth
        setListeners()
    }

    private fun setListeners() {
            editImg.setOnClickListener {
            Intent(
                  Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                  ).also {
                  pickerIntent ->
                  pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                  //Getting a result from an activity
                  startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== REQUEST_CODE_PICK_IMAGE && resultCode== RESULT_OK)
        {
            data?.data?.let { imageUri->
                Intent(applicationContext, EditImgActivity::class.java)
                .also {  editImageIntent ->
                         editImageIntent.putExtra(KEY_IMAGE_URI,imageUri)
                         startActivity(editImageIntent)
                      }
                }
           }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.miLogout){
            Log.i(TAG,"Logout")
            //Logout the user
            auth.signOut()
            val logOutIntent =Intent(this, LoginActivity::class.java)
            logOutIntent.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logOutIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}