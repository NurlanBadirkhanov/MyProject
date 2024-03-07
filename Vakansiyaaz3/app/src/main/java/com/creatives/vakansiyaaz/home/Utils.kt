package com.creatives.vakansiyaaz.home

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem.MyAdsAdapter
import com.creatives.vakansiyaaz.home.adapter.Home2Adapter
import com.creatives.vakansiyaaz.home.adapter.HomeAdapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

fun AppCompatActivity.exit() {
    finish()
}

fun Fragment.navigate(id: Int) {
    findNavController().navigate(id)
}

fun Fragment.popBack() {
    findNavController().popBackStack()

}


suspend fun fetchProductsFromCategory1(
    adapter: HomeAdapter,
    context: Context,
    categoryReference: DatabaseReference,
    powerbankProducts: MutableList<VacancyData>,
    progressBar: ProgressBar,
) {
    categoryReference.orderByKey()

        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (productSnapshot in snapshot.children) {
                    val title = productSnapshot.child("title").getValue(String::class.java)
                    val proAndLink =
                        productSnapshot.child("proAndLink").getValue(String::class.java)
                    val twoDaysInMillis =
                        productSnapshot.child("twoDaysInMillis").getValue(Long::class.java)
                    val weekInMillis =
                        productSnapshot.child("weekInMillis").getValue(Long::class.java)
                    val description = productSnapshot.child("desc").getValue(String::class.java)
                    val price = productSnapshot.child("price").getValue(String::class.java)
                    val city = productSnapshot.child("city").getValue(String::class.java)
                    val id = productSnapshot.child("id").getValue(String::class.java)
                    val verification =
                        productSnapshot.child("verification").getValue(Boolean::class.java)

                    val name = productSnapshot.child("name").getValue(String::class.java)
                    val number = productSnapshot.child("number").getValue(String::class.java)
                    val gmail = productSnapshot.child("gmail").getValue(String::class.java)


                    val companyName =
                        productSnapshot.child("nameOrg").getValue(String::class.java)
                    val uniqueId =
                        productSnapshot.child("uniqueId").getValue(String::class.java)
                    val work = productSnapshot.child("work").getValue(String::class.java)
                    val sphera = productSnapshot.child("sphera").getValue(String::class.java)
                    val experience =
                        productSnapshot.child("experience").getValue(String::class.java)
                    val education =
                        productSnapshot.child("education").getValue(String::class.java)
                    val timeWork = productSnapshot.child("timeWork").getValue(String::class.java)
                    val date = productSnapshot.child("data").getValue(Long::class.java)
                    val link = productSnapshot.child("link").getValue(String::class.java)
                    val idElement = productSnapshot.child("idElement").getValue(Int::class.java)


                    val product = VacancyData(
                        idElement = idElement ?: 0,
                        link = link ?: "",
                        sphera = sphera ?: "",
                        education = education ?: "",
                        timeWork = timeWork ?: "",
                        experience = experience ?: "",
                        id = id ?: "",
                        companyName = companyName ?: "",
                        title = title ?: "",
                        price = price ?: "",
                        city = city ?: "",
                        desc = description ?: "",
                        name = name ?: "",
                        number = number ?: "",
                        gmail = gmail ?: "",
                        data = date ?: 0,
                        verification = verification ?: false,
                        uniqueId = uniqueId ?: "",
                        work = work ?: "",
                        proAndLink = proAndLink ?: "",
                        twoDaysInMillis = twoDaysInMillis ?: 0,
                        weekInMillis = weekInMillis ?: 0,
                    )

                    powerbankProducts.add(product)
                }


                adapter.setData(powerbankProducts)

                progressBar.visibility = View.GONE


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
}


suspend fun fetchProductsFromCategoryHome2Adapter(
    adapter: Home2Adapter,
    categoryReference: DatabaseReference,
    powerbankProducts: MutableList<VacancyData>,
    progressBar: ProgressBar,
) {
    categoryReference.orderByKey()

        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (productSnapshot in snapshot.children) {
                    val title = productSnapshot.child("title").getValue(String::class.java)
                    val proAndLink = productSnapshot.child("proAndLink").getValue(String::class.java)
                    val twoDaysInMillis = productSnapshot.child("twoDaysInMillis").getValue(Long::class.java)
                    val weekInMillis = productSnapshot.child("weekInMillis").getValue(Long::class.java)
                    val description = productSnapshot.child("desc").getValue(String::class.java)
                    val price = productSnapshot.child("price").getValue(String::class.java)
                    val city = productSnapshot.child("city").getValue(String::class.java)
                    val id = productSnapshot.child("id").getValue(String::class.java)
                    val verification = productSnapshot.child("verification").getValue(Boolean::class.java)
                    val name = productSnapshot.child("name").getValue(String::class.java)
                    val number = productSnapshot.child("number").getValue(String::class.java)
                    val gmail = productSnapshot.child("gmail").getValue(String::class.java)
                    val companyName = productSnapshot.child("nameOrg").getValue(String::class.java)
                    val uniqueId = productSnapshot.child("uniqueId").getValue(String::class.java)
                    val work = productSnapshot.child("work").getValue(String::class.java)
                    val sphera = productSnapshot.child("sphera").getValue(String::class.java)
                    val experience = productSnapshot.child("experience").getValue(String::class.java)
                    val education = productSnapshot.child("education").getValue(String::class.java)
                    val timeWork = productSnapshot.child("timeWork").getValue(String::class.java)
                    val date = productSnapshot.child("data").getValue(Long::class.java)
                    val link = productSnapshot.child("link").getValue(String::class.java)
                    val idElement = productSnapshot.child("idElement").getValue(Int::class.java)


                    val product = VacancyData(
                        idElement = idElement ?: 0,
                        link = link ?: "",
                        sphera = sphera ?: "",
                        education = education ?: "",
                        timeWork = timeWork ?: "",
                        experience = experience ?: "",
                        id = id ?: "",
                        companyName = companyName ?: "",
                        title = title ?: "",
                        price = price ?: "",
                        city = city ?: "",
                        desc = description ?: "",
                        name = name ?: "",
                        number = number ?: "",
                        gmail = gmail ?: "",
                        data = date ?: 0,
                        verification = verification ?: false,
                        uniqueId = uniqueId ?: "",
                        work = work ?: "",
                        proAndLink = proAndLink ?: "",
                        twoDaysInMillis = twoDaysInMillis ?: 0,
                        weekInMillis = weekInMillis ?: 0,
                    )

                    powerbankProducts.add(product)
                }


                adapter.setData(powerbankProducts)

                progressBar.visibility = View.GONE


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
}

fun fetchProductsFromCategoryMyAds(
    adapter: MyAdsAdapter,
    context: Context,
    categoryReference: DatabaseReference,
    powerbankProducts: MutableList<VacancyData>,
    progressBar: ProgressBar,
) {
    categoryReference.orderByKey()

        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (productSnapshot in snapshot.children) {
                    val title = productSnapshot.child("title").getValue(String::class.java)
                    val proAndLink =
                        productSnapshot.child("proAndLink").getValue(String::class.java)
                    val twoDaysInMillis =
                        productSnapshot.child("twoDaysInMillis").getValue(Long::class.java)
                    val weekInMillis =
                        productSnapshot.child("weekInMillis").getValue(Long::class.java)
                    val description = productSnapshot.child("desc").getValue(String::class.java)
                    val price = productSnapshot.child("price").getValue(String::class.java)
                    val city = productSnapshot.child("city").getValue(String::class.java)
                    val id = productSnapshot.child("id").getValue(String::class.java)
                    val verification =
                        productSnapshot.child("verification").getValue(Boolean::class.java)
                    val name = productSnapshot.child("name").getValue(String::class.java)
                    val number = productSnapshot.child("number").getValue(String::class.java)
                    val gmail = productSnapshot.child("gmail").getValue(String::class.java)
                    val companyName = productSnapshot.child("nameOrg").getValue(String::class.java)
                    val uniqueId = productSnapshot.child("uniqueId").getValue(String::class.java)
                    val work = productSnapshot.child("work").getValue(String::class.java)
                    val sphera = productSnapshot.child("sphera").getValue(String::class.java)
                    val experience =
                        productSnapshot.child("experience").getValue(String::class.java)
                    val education = productSnapshot.child("education").getValue(String::class.java)
                    val timeWork = productSnapshot.child("timeWork").getValue(String::class.java)
                    val date = productSnapshot.child("data").getValue(Long::class.java)
                    val link = productSnapshot.child("link").getValue(String::class.java)
                    val idElement = productSnapshot.child("idElement").getValue(Int::class.java)


                    val product = VacancyData(
                        idElement = idElement ?: 0,
                        link = link ?: "",
                        sphera = sphera ?: "",
                        education = education ?: "",
                        timeWork = timeWork ?: "",
                        experience = experience ?: "",
                        id = id ?: "",
                        companyName = companyName ?: "",
                        title = title ?: "",
                        price = price ?: "",
                        city = city ?: "",
                        desc = description ?: "",
                        name = name ?: "",
                        number = number ?: "",
                        gmail = gmail ?: "",
                        data = date ?: 0,
                        verification = verification ?: false,
                        uniqueId = uniqueId ?: "",
                        work = work ?: "",
                        proAndLink = proAndLink ?: "",
                        twoDaysInMillis = twoDaysInMillis ?: 0,
                        weekInMillis = weekInMillis ?: 0,
                    )

                    powerbankProducts.add(product)
                }


                adapter.setData(powerbankProducts)

                progressBar.visibility = View.GONE


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
}


