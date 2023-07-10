package com.ktxdevelopment.productionanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktxdevelopment.productionanalyzer.databinding.FragmentResultBinding

class FragmentResult(private var results: ArrayList<MainModel>) : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var rAdapter: MainResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tVolume = 0.0
        var tRoyalty = 0.0
        var tCashIn = 0.0
        var tCapex = 0.0
        var tDepr = 0.0
        var tOpex = 0.0
        var tTax = 0.0
        var tCashSd = 0.0
        var tCashSdPV = 0.0
        var tDiscount = 0.0

        results.forEach {
            tVolume += it.volume.toDouble()
            tRoyalty += it.royalty.toDouble()
            tCashIn += it.cashIn.toDouble()
            tCapex += it.capex.toDouble()
            tOpex += it.opex.toDouble()
            tTax += it.tax.toDouble()
            tDepr += it.depr.toDouble()
            tCashSd += it.cashSV.toDouble()
            tCashSdPV += it.cashSvPV.toDouble()
            tDiscount += it.discountFactor.toDouble()
        }
        results.add(MainModel("Total", tVolume.roundDec(), tCashIn.roundDec(), tCapex.roundDec(), tRoyalty.roundDec(), tOpex.roundDec(), tDepr.roundDec(), tTax.roundDec(), tCashSd.roundDec(), tCashSdPV.roundDec(), tDiscount.roundDec()))
        rAdapter = MainResultAdapter(results)
        try {
            binding.rvResult.apply {
                adapter = rAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }catch (_: Exception) {}
    }
}