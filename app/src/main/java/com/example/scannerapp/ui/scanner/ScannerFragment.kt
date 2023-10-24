package com.example.scannerapp.ui.scanner

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.*
import com.bumptech.glide.Glide
import com.example.scannerapp.*
import com.example.scannerapp.databinding.FragmentScannerBinding
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_scanner.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScannerFragment : Fragment() {

    private var _binding: FragmentScannerBinding? = null
    private lateinit var codeScanner: CodeScanner
    private var imageUrl: String = ""
    private var imageUri: Uri? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeScanner = CodeScanner(requireContext(), scanner)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback { result ->
            if (result.text.isNotEmpty()) {
                val scannerData = result.text
                getScanner(scannerData!!)
                coordinatorLayout.visibility = View.VISIBLE
            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            Toast.makeText(requireActivity(), "Scan result: ${"Error"}", Toast.LENGTH_LONG).show()
        }

        scanner.setOnClickListener {
            codeScanner.startPreview()
            title.text = ""
            brand.text = ""
            Glide
                .with(this@ScannerFragment)
                .load("")
                .into(image)
            coordinatorLayout.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun scannedProductsRegister() {

        if (imageUri != null) {
            val scannedProducts = ScannedProducts(
                title.text.toString(),
                brand.text.toString(),
                listOf(imageUri.toString())
            )
            FirestoreClass().registerScannedProducts(this, scannedProducts)

        }
    }

    private fun getScanner(scannerData: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiInterface = retrofit.create(ApiInterface::class.java)

        val call: Call<ApiResponse> = service.getApi(
            scannerData, "y", Constants.API_KEY
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiList = response.body()
                    if (apiList != null && apiList.products.isNotEmpty()) {
                        title.text = apiList.products[0].title
                        brand.text = apiList.products[0].brand
                        Glide
                            .with(this@ScannerFragment)
                            .load(apiList.products[0].images[0])
                            .into(image)
                        imageUri = Uri.parse(apiList.products[0].images[0])
                        scannedProductsRegister()

                    } else {
                        Toast.makeText(requireActivity(), "No products found", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
            }
        })
    }


}
