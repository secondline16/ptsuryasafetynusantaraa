package com.ssn.app.ui.job_vacancy.index

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssn.app.R
import com.ssn.app.databinding.ItemJobVacancyBinding
import com.ssn.app.extension.inflateBinding
import com.ssn.app.helper.DateHelper
import com.ssn.app.model.JobVacancy

class JobVacancyAdapter(
    var onClickListener: ((JobVacancy) -> Unit)? = null
) : ListAdapter<JobVacancy, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ItemViewHolder(
        parent.inflateBinding(ItemJobVacancyBinding::inflate)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder as ItemViewHolder).bind(item)
        }
    }

    private inner class ItemViewHolder(
        private val binding: ItemJobVacancyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JobVacancy) = with(binding) {
            tvCompanyName.text = item.companyName
            tvJobName.text = item.jobPosition
            val deadlineDate = DateHelper.changeFormat(
                dateString = item.deadline,
                oldFormat = DateHelper.API_DATE_PATTERN,
                newFormat = DateHelper.VIEW_DATE_PATTERN
            )
            tvDeadline.text = root.context.getString(
                R.string.formatter_deadline,
                deadlineDate
            )

            root.setOnClickListener { onClickListener?.invoke(item) }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<JobVacancy>() {
            override fun areItemsTheSame(
                oldItem: JobVacancy,
                newItem: JobVacancy
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: JobVacancy,
                newItem: JobVacancy
            ): Boolean = oldItem == newItem
        }
    }
}
