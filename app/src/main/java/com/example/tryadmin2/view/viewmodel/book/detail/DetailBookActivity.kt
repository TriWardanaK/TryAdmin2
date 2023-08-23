package com.example.tryadmin2.view.viewmodel.book.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tryadmin2.databinding.ActivityDetailBookBinding
import com.example.tryadmin2.databinding.DetailAppbarBinding
import com.example.tryadmin2.databinding.DetailDeskripsiBinding
import com.example.tryadmin2.databinding.DetailInfoBukuBinding
import com.example.tryadmin2.util.RequestState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var appbarBinding: DetailAppbarBinding
    private lateinit var infoBukuBinding: DetailInfoBukuBinding
    private lateinit var deskripsiBinding: DetailDeskripsiBinding
    private val viewModel: DetailBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        binding.apply {
            appbarBinding = detailAppbar
            infoBukuBinding = detailInfoBuku
            deskripsiBinding = detailDeskripsi
        }
        setContentView(binding.root)

        viewModel.getDetailBook(getId())
        getDetailBook()
    }

    private fun getId(): Int {
        return intent.getIntExtra("idBook", 0)
    }

    private fun getDetailBook() {
        viewModel.dataBookResponse.observe(this) {
            when (it) {
                is RequestState.Loading -> {}
                is RequestState.Success -> {
                    binding.apply {
                        it.data?.apply {
                            appbarBinding.apply {
                                infoBukuBinding.apply {
                                    deskripsiBinding.apply {
                                        collapsingBackdropTitle.title = judulBuku
                                        tvJudulBuku.text = judulBuku
                                        tvValuePenulisBuku.text = penulisBuku
                                        tvValuePenerbitBuku.text = penerbitBuku
                                        tvValueDeskripsiBuku.text = deskripsiBuku
                                        tvValueTahunTerbit.text = tahunTerbit.toString()
                                        tvValueJumlahHalaman.text = jumlahHalaman.toString()
                                        tvValueStok.text = stokBuku.toString()

                                        Picasso.get().load(coverBuku).into(ivBackdropDetail)
                                        Picasso.get().load(coverBuku).into(ivCoverDetail)
                                    }
                                }
                            }
                        }
                    }
                }
                is RequestState.Error -> {
                    Toast.makeText(this@DetailBookActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}