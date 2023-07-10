package com.ktxdevelopment.productionanalyzer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.ktxdevelopment.productionanalyzer.databinding.FragmentMainBinding
import kotlin.math.pow
import kotlin.math.roundToInt

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var capexAdapter: LinkedCapexAdapter
    private lateinit var capexList: ArrayList<CapexModel>

    private var _opex = 0.0
    private var _decline = 0.0
    private var _discount = 0.0
    private var _royalty = 0.0
    private var _life = 0
    private var _price = 0.0
    private var _tax = 0.0
    private var totalVolume = 0.0
    private var totalCapex = 0.0
    private var _volume = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        capexList = arrayListOf()

        initUi()

        try {
            capexAdapter = LinkedCapexAdapter(capexList)
            binding.rvLinkedImages.apply {
                adapter = capexAdapter
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            }
        } catch (_: Exception) {
        }
    }

    private fun initUi() {
        binding.btnSubmit.setOnClickListener {
            if (validateInput()) {
                _life = binding.etLife.text.toString().toInt()
                _opex = binding.etOpex.text.toString().toDouble()
                _price = binding.etPrice.text.toString().toDouble()
                _decline = binding.etDecline.text.toString().toDouble()
                _discount = binding.etDiscountRate.text.toString().toDouble()
                _royalty = binding.etRoyalty.text.toString().toDouble()
                _tax = binding.etTax.text.toString().toDouble()
                _volume = binding.etVolume.text.toString().toDouble()

                totalVolume = calculateTotalVolume()
                totalCapex = calculateTotalCapex()

                val results = arrayListOf<MainModel>()

                var currentVolume = _volume

                for (i in 1.._life) {
                    var capex: Double? = null
                    for (c in capexList) { if (c.year == i) capex = c.capex }
                    if (i > 2) currentVolume *= (1 - _decline / 100)
                    results.add(calculateResult(currentVolume, i, capex))
                }
                try {
                    (requireActivity() as MainActivity).navigateToResults(results)
                }catch (_: Exception) {}
            }
        }

        binding.btnAddCapex.setOnClickListener {
            if (validateFilledInput(binding.etCapex) && validateFilledInput(binding.etCapexYear)) {
                val capex = binding.etCapex.text.toString().trim { it < ' ' }.toDouble()
                val year = binding.etCapexYear.text.toString().trim { it < ' ' }.toInt()
                capexAdapter.addItem(CapexModel(capex, year))
            }
        }
    }


    private fun calculateResult(volume: Double, year: Int, inCapex: Double?): MainModel {
        val capex: Double = inCapex ?: 0.0

        if (year == 1) {
            return if (inCapex == null) MainModel(0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0)
            else MainModel(1,0.0,0.0,capex,0.0,0.0,0.0,0.0,-capex,-capex,1.0)
        }

        val cashIn = volume * _price
        val royalty = _royalty /100 * cashIn
        val opex = _opex
        val depr = totalCapex * volume / totalVolume
        val tax = _tax / 100 * (cashIn - royalty - opex - depr)
        val cashSD = cashIn - royalty - capex - opex - tax
        val discountFactor = (1 / (1 + _discount / 100)).pow(year-1)
        val cashSdPV = cashSD * discountFactor
        return MainModel(year, volume, cashIn, capex, royalty, opex, depr, tax, cashSD, cashSdPV, discountFactor)
    }

    private fun calculateTotalVolume(): Double {
        var totalVolume = _volume
        var cv = _volume
        for (i in 2 until _life) {
            cv *= (1 - _decline / 100)
            totalVolume += cv
        }
        return totalVolume
    }


    private fun calculateTotalCapex(): Double {
        var totalCapex = 0.0
        for (i in capexList) { totalCapex += i.capex }
        return totalCapex
    }


    private fun validateInput(): Boolean {
        var isError = false
        if (!validateFilledInput(binding.etDecline)) isError = true
        if (!validateFilledInput(binding.etLife)) isError = true
        if (!validateFilledInput(binding.etOpex)) isError = true
        if (!validateFilledInput(binding.etPrice)) isError = true
        if (!validateFilledInput(binding.etTax)) isError = true
        if (!validateFilledInput(binding.etDiscountRate)) isError = true
        if (!validateFilledInput(binding.etRoyalty)) isError = true
        if (!validateFilledInput(binding.etVolume)) isError = true
        if (!validateCapex(capexList)) isError = true
        return !isError
    }

    private fun validateCapex(capexList: ArrayList<CapexModel>): Boolean {
        try {
            if (capexList.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.enter_capex), Toast.LENGTH_SHORT).show()
                return false
            } else {
                for (i in capexList) {
                    if (i.year > binding.etLife.text.toString().trim { it < ' ' }.toInt()) {
                        Toast.makeText(requireContext(), getString(R.string.no_more_than_life), Toast.LENGTH_SHORT).show()
                        return false
                    }else if(i.year < 1) {
                        Toast.makeText(requireContext(), "Capex must be higher than 1", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
                return true
            }
        } catch (e: Exception) {
            return false
        }
    }

    private fun validateFilledInput(et: EditText): Boolean {
        val pass = et.text.toString()
        return if (pass.isBlank()) { getParentTilOf(et).error = "Enter the field"; false
        } else { getParentTilOf(et).error = "";true }
    }


    private fun getParentTilOf(et: EditText): TextInputLayout = (et.parent.parent as TextInputLayout)
}

data class MainModel(var year: String, var volume: String, var cashIn: String, var capex: String, var royalty: String, var opex: String, var depr: String, var tax: String, var cashSV: String, var cashSvPV: String, var discountFactor: String) {
    constructor(year: Int, volume: Double, cashIn: Double, capex: Double, royalty: Double, opex: Double, depr: Double, tax: Double, cashSV: Double, cashSvPV: Double, discountFactor: Double
    ):this(
        year.toString(),
        volume.roundDec().toString(),
        cashIn.roundToInt().toString(),
        capex.roundToInt().toString(),
        royalty.roundToInt().toString(),
        opex.roundToInt().toString(),
        depr.roundToInt().toString(),
        tax.roundToInt().toString(),
        cashSV.roundToInt().toString(),
        cashSvPV.roundToInt().toString(),
        discountFactor.roundDec().toString())

    constructor(year: String, volume: Double, cashIn: Double, capex: Double, royalty: Double, opex: Double, depr: Double, tax: Double, cashSV: Double, cashSvPV: Double, discountFactor: Double
    ):this(year, volume.toString(), cashIn.toString(), capex.toString(), royalty.toString(), opex.toString(), depr.toString(), tax.toString(), cashSV.toString(), cashSvPV.toString(), discountFactor.toString())
}

fun Double.roundDec(): Double {
    return (100 * this).roundToInt() / 100.0
}