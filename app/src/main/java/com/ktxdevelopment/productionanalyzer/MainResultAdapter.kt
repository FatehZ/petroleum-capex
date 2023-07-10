package com.ktxdevelopment.productionanalyzer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ktxdevelopment.productionanalyzer.databinding.ResultItemBinding

class MainResultAdapter(private var l: ArrayList<MainModel>) : RecyclerView.Adapter<MainResultAdapter.MainViewHolder>() {

    private var list: ArrayList<MainModel> = arrayListOf<MainModel>().apply {
        add(MainModel("Year", "Volume", "Cash In", "Capex", "Royalty", "Opex", "Deprecation", "Tax", "Cash S/D", "Cash S/D PV", "Discount"))
        addAll(l)
    }

    inner class MainViewHolder(var binding: ResultItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvYear.text = model.year
        holder.binding.tvVolume.text = model.volume
        holder.binding.tvCashIn.text = model.cashIn
        holder.binding.tvCapex.text = model.capex
        holder.binding.tvRoyalty.text = model.royalty
        holder.binding.tvOpex.text = model.opex
        holder.binding.tvDepr.text = model.depr
        holder.binding.tvTax.text = model.tax
        holder.binding.tvDiscount.text = model.discountFactor
        holder.binding.tvCashSD.text = model.cashSV
        holder.binding.tvCashSdPV.text = model.cashSvPV
    }

    override fun getItemCount() = list.size
}