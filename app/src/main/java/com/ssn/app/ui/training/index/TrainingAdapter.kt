package com.ssn.app.ui.training.index

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.app.R
import com.ssn.app.databinding.ItemTrainingBinding
import com.ssn.app.extension.inflateBinding
import com.ssn.app.extension.toCurrencyFormat
import com.ssn.app.helper.DateHelper
import com.ssn.app.model.Training

class TrainingAdapter(
    var onClickListener: ((Training) -> Unit)? = null
) : ListAdapter<Training, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ItemViewHolder(
        parent.inflateBinding(ItemTrainingBinding::inflate)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder as ItemViewHolder).bind(item)
        }
    }

    private inner class ItemViewHolder(
        private val binding: ItemTrainingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Training) = with(binding) {
            tvName.text = item.trainingName
            val startDate = DateHelper.changeFormat(
                dateString = item.trainingStartDate,
                oldFormat = DateHelper.API_DATE_PATTERN,
                newFormat = DateHelper.VIEW_DATE_PATTERN
            )
            val endDate = DateHelper.changeFormat(
                dateString = item.trainingEndDate,
                oldFormat = DateHelper.API_DATE_PATTERN,
                newFormat = DateHelper.VIEW_DATE_PATTERN
            )
            tvSchedule.text = root.context.getString(
                R.string.formatter_dash_divider,
                startDate,
                endDate
            )
            tvPrice.text = item.trainingPrice.toCurrencyFormat()

            root.setOnClickListener { onClickListener?.invoke(item) }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Training>() {
            override fun areItemsTheSame(
                oldItem: Training,
                newItem: Training
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Training,
                newItem: Training
            ): Boolean = oldItem == newItem
        }
    }
}
