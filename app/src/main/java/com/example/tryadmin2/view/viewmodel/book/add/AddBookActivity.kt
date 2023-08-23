package com.example.tryadmin2.view.viewmodel.book.add

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tryadmin2.databinding.ActivityAddBookBinding
import com.example.tryadmin2.databinding.ItemUploadImageBinding
import com.example.tryadmin2.util.RequestState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookActivity : AppCompatActivity() {

    private val viewModel: AddBookViewModel by viewModels()
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var uploadImageBinding: ItemUploadImageBinding
    private lateinit var storageRef: StorageReference
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private val pickImageRequest = "image/*"
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        uploadImageBinding = binding.layoutUploadImage
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance().reference

        galleryLauncher()

        uploadImageBinding.selectImage.setOnClickListener {
            openGallery()
        }

        uploadImageBinding.uploadImage.setOnClickListener {
            uploadImage()
        }

        binding.btnSave.setOnClickListener {
            postBook()
        }
        dataBookResponse()
    }

    private fun galleryLauncher() {
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    selectedImageUri = it
                    uploadImageBinding.selectedImage.setImageURI(it)
                }
            }
    }

    private fun openGallery() {
        galleryLauncher.launch(pickImageRequest)
    }

    private fun uploadImage() {
        showLoading()
        if (selectedImageUri != null) {
            val imageRef = storageRef.child("${System.currentTimeMillis()}.jpg")

            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        binding.edtCoverBuku.setText(imageUrl)
                        hideLoading()
                        Toast.makeText(
                            this@AddBookActivity,
                            "Data Berhasil Diupload",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                        .addOnFailureListener { exception ->
                            hideLoading()
                            Toast.makeText(
                                this@AddBookActivity,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }
    }

    private fun postBook() {
        binding.apply {
            val judulBuku = edtJudulBuku.text.toString()
            val penulisBuku = edtPenulisBuku.text.toString()
            val penerbitBuku = edtPenerbitBuku.text.toString()
            val tahunTerbit = edtTahunTerbit.text.toString()
            val jumlahHalaman = edtJumlahHalamam.text.toString()
            val stokBuku = edtStokBuku.text.toString()
            val coverBuku = edtCoverBuku.text.toString()
            val deskripsiBuku = edtDeskripsiBuku.text.toString()
            when {
                judulBuku.isEmpty() -> {
                    edtJudulBuku.error = "Masih kosong"
                }
                penulisBuku.isEmpty() -> {
                    edtPenulisBuku.error = "Masih kosong"
                }
                penerbitBuku.isEmpty() -> {
                    edtPenerbitBuku.error = "Masih kosong"
                }
                tahunTerbit.isEmpty() -> {
                    edtTahunTerbit.error = "Masih kosong"
                }
                jumlahHalaman.isEmpty() -> {
                    edtJumlahHalamam.error = "Masih kosong"
                }
                stokBuku.isEmpty() -> {
                    edtStokBuku.error = "Masih kosong"
                }
                coverBuku.isEmpty() -> {
                    edtCoverBuku.error = "Masih kosong"
                }
                deskripsiBuku.isEmpty() -> {
                    edtDeskripsiBuku.error = "Masih kosong"
                }
                else -> {
                    viewModel.postBook(
                        judulBuku,
                        penulisBuku,
                        penerbitBuku,
                        tahunTerbit.toInt(),
                        jumlahHalaman.toInt(),
                        stokBuku.toInt(),
                        coverBuku,
                        deskripsiBuku
                    )
                }
            }
        }
    }

    private fun dataBookResponse() {
        viewModel.dataBookResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    Toast.makeText(
                        this@AddBookActivity,
                        "Data Berhasil Disimpan",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.apply {
                        edtJudulBuku.text.clear()
                        edtPenulisBuku.text.clear()
                        edtPenerbitBuku.text.clear()
                        edtTahunTerbit.text.clear()
                        edtJumlahHalamam.text.clear()
                        edtStokBuku.text.clear()
                        edtCoverBuku.text.clear()
                        edtDeskripsiBuku.text.clear()
                        uploadImageBinding.selectedImage.setImageDrawable(null)
                    }
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@AddBookActivity, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbAddBook.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbAddBook.visibility = View.GONE
    }
}