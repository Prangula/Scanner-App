package com.example.scannerapp.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scannerapp.FirestoreClass
import com.example.scannerapp.ProductsAdapter
import com.example.scannerapp.ScannedProducts
import com.example.scannerapp.databinding.FragmentProductsBinding
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        homeViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getScannedProducts()


    }

    private fun getScannedProducts(){

        FirestoreClass().getScannedProducts(this@ProductsFragment)

    }

    override fun onResume() {
        super.onResume()
        getScannedProducts()
    }

    fun productsSuccess(items:ArrayList<ScannedProducts>){

        if(items.size>0){

            val adapter = ProductsAdapter(items)
            scannedRv.adapter = adapter
            scannedRv.layoutManager = LinearLayoutManager(activity)
            products_noScanned.visibility = View.GONE

        }else{
            products_noScanned.visibility = View.VISIBLE

        }

    }
}