package com.example.tryadmin2.view.viewmodel.book.update

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tryadmin2.databinding.ActivityUpdateBookBinding
import com.example.tryadmin2.databinding.ItemUploadImageBinding
import com.example.tryadmin2.util.RequestState
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBookBinding
    private val viewModel: UpdateBookViewModel by viewModels()
    private lateinit var uploadImageBinding: ItemUploadImageBinding
    private lateinit var storageRef: StorageReference
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private val pickImageRequest = "image/*"
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
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

        viewModel.getDetailBook(getIdBook())
        getDetailBook()

        binding.btnUpdate.setOnClickListener {
            putBook()
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
                            this@UpdateBookActivity,
                            "Data Berhasil Diupload",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                        .addOnFailureListener { exception ->
                            hideLoading()
                            Toast.makeText(
                                this@UpdateBookActivity,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }
    }

    private fun getIdBook(): Int {
        return intent.getIntExtra("idBook", 0)
    }

    private fun getDetailBook() {
        viewModel.idDataBookResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    binding.apply {
                        it.data?.apply {
                            edtJudulBuku.setText(judulBuku)
                            edtPenulisBuku.setText(penulisBuku)
                            edtPenerbitBuku.setText(penerbitBuku)
                            edtTahunTerbit.setText(tahunTerbit.toString())
                            edtJumlahHalamam.setText(jumlahHalaman.toString())
                            edtStokBuku.setText(stokBuku.toString())
                            edtCoverBuku.setText(coverBuku)
                            edtDeskripsiBuku.setText(deskripsiBuku)
                        }
                    }
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@UpdateBookActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun putBook() {
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
                    viewModel.putBook(
                        getIdBook().toString(),
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
        viewModel.putDataBookResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> showLoading()
                is RequestState.Success -> {
                    hideLoading()
                    Toast.makeText(
                        this@UpdateBookActivity,
                        "Data Berhasil Diupdate",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is RequestState.Error -> {
                    hideLoading()
                    Toast.makeText(this@UpdateBookActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading() {
        binding.pbUpdateBook.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbUpdateBook.visibility = View.GONE
    }
}