package com.example.tryadmin2.view.viewmodel.admin.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tryadmin2.R
import com.example.tryadmin2.databinding.ActivityMainBinding
import com.example.tryadmin2.model.remote.response.book.DataBookResponse
import com.example.tryadmin2.util.RequestState
import com.example.tryadmin2.view.adapter.BookAdapter
import com.example.tryadmin2.view.viewmodel.admin.login.LoginActivity
import com.example.tryadmin2.view.viewmodel.admin.update.UpdateActivity
import com.example.tryadmin2.view.viewmodel.book.add.AddBookActivity
import com.example.tryadmin2.view.viewmodel.book.detail.DetailBookActivity
import com.example.tryadmin2.view.viewmodel.book.update.UpdateBookActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var recyclerAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getBook()
        observeBook()
        initRecyclerview()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBook()
        observeBook()
        initRecyclerview()
    }

    private fun getId(): Int {
        return intent.getIntExtra("idAdmin", 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun observeBook() {
        viewModel.dataBookResponse.observe(this) {
            if (it != null) {
                when (it) {
                    is RequestState.Loading -> showLoading()
                    is RequestState.Success -> {
                        hideLoading()
                        it.data?.data.let { data ->
                            if (data != null) {
                                recyclerAdapter.setListBook(data)
                            }
                        }
                    }
                    is RequestState.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun observeBookDel() {
        viewModel.deleteBookResponse.observe(this) {
            if (it != null) {
                when (it) {
                    is RequestState.Loading -> showLoading()
                    is RequestState.Success -> {
                        hideLoading()
                        viewModel.getBook()
                        observeBook()
                        initRecyclerview()
                        Toast.makeText(
                            this@MainActivity,
                            "Data Berhasil Dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is RequestState.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        binding.rvBuku.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = BookAdapter(BookAdapterListener())
        binding.rvBuku.adapter = recyclerAdapter
    }

    private fun showLoading() {
        binding.pbBook.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbBook.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_buku -> {
                val intent = Intent(this, AddBookActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                val intent = Intent(this, UpdateActivity::class.java)
                intent.putExtra("idAdmin", getId())
                startActivity(intent)
                true
            }
            R.id.logout -> {
                finishAffinity()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private inner class BookAdapterListener : BookAdapter.OnClickListener {
        override fun onDeleteClicked(book: DataBookResponse.BookResponse) {
            viewModel.deleteBook(book.idBuku)
            observeBookDel()
        }

        override fun onItemClicked(book: DataBookResponse.BookResponse) {
            val intent = Intent(this@MainActivity, DetailBookActivity::class.java)
            intent.putExtra("idBook", book.idBuku)
            startActivity(intent)
        }

        override fun onUpdateClicked(book: DataBookResponse.BookResponse) {
            val intent = Intent(this@MainActivity, UpdateBookActivity::class.java)
            intent.putExtra("idBook", book.idBuku)
            startActivity(intent)
        }
    }
}