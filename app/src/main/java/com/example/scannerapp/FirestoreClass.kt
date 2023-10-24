package com.example.scannerapp

import android.net.Uri
import android.util.Log
import com.example.scannerapp.ui.products.ProductsFragment
import com.example.scannerapp.ui.scanner.ScannerFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private var firestore = FirebaseFirestore.getInstance()

     fun registerScannedProducts(fragment: ScannerFragment,product: ScannedProducts){

         firestore.collection(Constants.SCANNED_PRODUCTS)
             .document()
             .set(product, SetOptions.merge())
             .addOnSuccessListener {

                 when(fragment){

                     is ScannerFragment ->{



                     }
                 }

             }
     }

    fun getScannedProducts(fragment:ProductsFragment){

        firestore.collection(Constants.SCANNED_PRODUCTS)
            .get()
            .addOnSuccessListener {document->

            val productsList:ArrayList<ScannedProducts> = ArrayList()

            for(i in document.documents){

                val product = i.toObject(ScannedProducts::class.java)!!
                product.id = i.id
                productsList.add(product)

            }

                fragment.productsSuccess(productsList)




            }

    }




}