package com.theworld.androidtemplatewithnavcomponents.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theworld.androidtemplatewithnavcomponents.data.response.PostOffice
import com.theworld.androidtemplatewithnavcomponents.databinding.LayoutDataBinding

class PostOfficeAdapter(private val listener: PostOfficeInterface) :
    ListAdapter<PostOffice, PostOfficeAdapter.CustomerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding =
            LayoutDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class CustomerViewHolder(private val binding: LayoutDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.imgMore.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(position))
                }
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    listener.onClick(getItem(position))

                }
            }
        }


        fun bind(item: PostOffice) {

            binding.apply {
                tvName.text = item.name

                val details = "${item.division}, ${item.state}"
                tvDescription.text = details
            }

        }


    }

    class DiffCallback : DiffUtil.ItemCallback<PostOffice>() {
        override fun areItemsTheSame(old: PostOffice, aNew: PostOffice) =
            old.pINCode == aNew.pINCode

        override fun areContentsTheSame(old: PostOffice, aNew: PostOffice) =
            old == aNew
    }


    interface PostOfficeInterface {
        fun onClick(postOffice: PostOffice)
     }

}