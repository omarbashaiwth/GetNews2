package com.omarahmed.getnews2.ui.explore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.databinding.ItemExploreCategoriesBinding

class CategoriesAdapter(
    lastPosition: Int,
    private val categories: ArrayList<Categories>,
    private val listener: OnUpdatePositionListener
) : BaseAdapter() {

    private var selectedPosition = lastPosition

    override fun getCount() = categories.size

    override fun getItem(position: Int) = categories[position]

    override fun getItemId(position: Int) = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val category = categories[position]
        val view =
            ItemExploreCategoriesBinding.inflate(LayoutInflater.from(parent.context))
        view.apply {
            ivCategory.setImageResource(category.image)
            tvCategoryName.text = category.title
            if (selectedPosition == position) {
                ivCategory.setColorFilter(ContextCompat.getColor(
                    ivCategory.context,
                    R.color.green
                ))
                tvCategoryName.setTextColor(
                    ContextCompat.getColor(
                        tvCategoryName.context,
                        R.color.green
                    )
                )
            }
            root.setOnClickListener {
                if (selectedPosition == position){
                    selectedPosition = -1
                    notifyDataSetChanged()
                }
                selectedPosition = position
                listener.onUpdatePosition(position)
                notifyDataSetChanged()
            }
        }
        return view.root
    }

    interface OnUpdatePositionListener{
        fun onUpdatePosition(position: Int)
    }
}