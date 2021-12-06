package com.mvlprem.apod.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvlprem.apod.databinding.ApodListBinding
import com.mvlprem.apod.domain.Pictures

/**
 * RecyclerView Adapter to create views and bind data
 * @param clickListener RecyclerView Items clickListener Class
 */
class PicturesAdapter(private val clickListener: ItemClickListener) :
    ListAdapter<Pictures, PicturesAdapter.ViewHolder>(DiffCall) {

    /**
     * Called when RecyclerView needs a new view of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflater = ApodListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    /**
     * ViewHolder for picture items. All work is done by data binding.
     */
    class ViewHolder(private val binding: ApodListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pictures: Pictures, clickListener: ItemClickListener) {
            binding.pictures = pictures
            binding.clickListner = clickListener
            binding.executePendingBindings()
        }
    }

    /**
     *  Checks the differences in old & new list
     */
    companion object DiffCall : DiffUtil.ItemCallback<Pictures>() {
        override fun areItemsTheSame(oldItem: Pictures, newItem: Pictures): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Pictures, newItem: Pictures): Boolean {
            return oldItem == newItem
        }
    }
}

/**
 * ClickListener for RecyclerView Items
 */
class ItemClickListener(val clickListener: (pictures: Pictures) -> Unit) {
    /**
     * called when item is clicked
     * @param pictures picture that has been clicked
     */
    fun onClick(pictures: Pictures) = clickListener(pictures)
}