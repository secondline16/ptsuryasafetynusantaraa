package com.ssn.app.ui.training_following.index

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.app.databinding.ItemTrainingFollowingBinding
import com.ssn.app.extension.inflateBinding
import com.ssn.app.model.TrainingFollowing

class TrainingFollowingAdapter(
    var onClickListener: ((TrainingFollowing) -> Unit)? = null
) : ListAdapter<TrainingFollowing, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ItemViewHolder(
        parent.inflateBinding(ItemTrainingFollowingBinding::inflate)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder as ItemViewHolder).bind(item)
        }
    }

    private inner class ItemViewHolder(
        private val binding: ItemTrainingFollowingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrainingFollowing) = with(binding) {
            tvName.text = item.trainingName
            tvStatus.text = item.trainingStatus

            root.setOnClickListener { onClickListener?.invoke(item) }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<TrainingFollowing>() {
            override fun areItemsTheSame(
                oldItem: TrainingFollowing,
                newItem: TrainingFollowing
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TrainingFollowing,
                newItem: TrainingFollowing
            ): Boolean = oldItem == newItem
        }
    }
}
