package com.submission.app.story.story.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.databinding.ActivityAddStoryBinding
import com.submission.app.story.shared.components.CustomButton
import com.submission.app.story.shared.components.TextField
import com.submission.app.story.shared.utils.ViewModelFactory
import com.submission.app.story.shared.utils.rotateBitmap
import com.submission.app.story.shared.utils.uriToFile
import com.submission.app.story.story.viewmodels.StoryViewModel
import java.io.File
import java.util.*
import kotlin.concurrent.schedule

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var edDescription: TextField
    private lateinit var btnUpload: CustomButton
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        supportActionBar?.apply {
            title = "Add Story"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            this@AddStoryActivity.edDescription = edDescription
            btnUpload = uploadButton
            cameraXButton.setOnClickListener {
                startCameraX()
            }
            galleryButton.setOnClickListener {
                startGallery()
            }

        }

        handleUploadImage()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(AuthPref.getInstance(dataStore))
        storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
    }

    private fun handleUploadImage() {
        btnUpload.setOnClickListener {
            if (getFile != null) {
                storyViewModel.getCredential().observe(this) { cred ->
                    storyViewModel.uploadImage(cred.token, getFile!!, edDescription.text.toString())
                }
            } else {
                Toast.makeText(this@AddStoryActivity, "Silahkan pilih gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }

        storyViewModel.isLoading.observe(this) {loading ->
            btnUpload.isEnabled = !loading
        }

        storyViewModel.genericResponse.observe(this) {
            it.getContentIfNotHandled()?.let { response ->
                if (response.error) {
                    Toast.makeText(this@AddStoryActivity, response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AddStoryActivity, response.message, Toast.LENGTH_SHORT).show()
                    Timer().schedule(1000) {
                        this@AddStoryActivity.runOnUiThread {
                            setResult(ListStoryActivity.ADD_STORY_RESULT)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }
}