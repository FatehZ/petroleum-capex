package com.ktxdevelopment.productionanalyzer
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktxdevelopment.productionanalyzer.databinding.ItemCapexListBinding

class LinkedCapexAdapter(private var list: MutableList<CapexModel>) : RecyclerView.Adapter<LinkedCapexAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCapexListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    class ViewHolder(val binding: ItemCapexListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvCapex.text = list[position].capex.toString()
        holder.binding.tvYear.text = list[position].year.toString()

        holder.binding.btnCancel.setOnClickListener { removeItem(list[position]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeItem(model: CapexModel) {

        for (i in list.indices) {
            if (list[i] === model) {
                list.removeAt(i)
                notifyDataSetChanged()
                return
            }
        }
    }

    fun addItem(item: CapexModel) {
        var exists = false
        var index = 0
        for (k in list.indices) {
            if (list[k].year == item.year) {
                exists = true
                list[k].capex = list[k].capex + item.capex
                notifyItemChanged(k)
            }
        }
        if (!exists) {
            list.add(item)
            notifyItemInserted(list.size - 1)
        }
    }

    override fun getItemCount(): Int = list.size
}









