package com.example.tryadmin2.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tryadmin2.databinding.ItemBookBinding
import com.example.tryadmin2.model.remote.response.book.DataBookResponse

class BookAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private var listBook = ArrayList<DataBookResponse.BookResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListBook(listBook: ArrayList<DataBookResponse.BookResponse>) {
        this.listBook = listBook
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBook[position])
    }

    inner class ViewHolder(private var binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookResponse: DataBookResponse.BookResponse) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(bookResponse.coverBuku)
                    .into(photoBuku)

                tvJudulBuku.text = bookResponse.judulBuku
                tvDeskripsiBuku.text = bookResponse.deskripsiBuku
            }
            setClickListeners(bookResponse)
        }

        private fun setClickListeners(bookResponse: DataBookResponse.BookResponse){
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    onClickListener.onItemClicked(bookResponse)
                }
                binding.btnSetHapus.setOnClickListener {
                    onClickListener.onDeleteClicked(bookResponse)
                }
                binding.btnSetUbah.setOnClickListener {
                    onClickListener.onUpdateClicked(bookResponse)
                }
            }
        }
    }

    interface OnClickListener {
        fun onDeleteClicked(book: DataBookResponse.BookResponse)
        fun onItemClicked(book: DataBookResponse.BookResponse)
        fun onUpdateClicked(book: DataBookResponse.BookResponse)
    }
}