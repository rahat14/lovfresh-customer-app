package com.lovfreshuser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovfreshuser.HelperClass
import com.lovfreshuser.adapters.BasicCategoryAdapter
import com.lovfreshuser.databinding.FragmentHomeBinding
import com.lovfreshuser.models.BasicCategoryModel
import com.lovfreshuser.networking.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), BasicCategoryAdapter.Interaction {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdatper: BasicCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        mAdatper = BasicCategoryAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBasicCategories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdatper
        }
        loadTheList()
    }

    private fun loadTheList() {
        val catCall = ApiProvider.dataApi.getBasicCategoryList()
        catCall.enqueue(object : Callback<List<BasicCategoryModel>> {
            override fun onResponse(
                call: Call<List<BasicCategoryModel>>,
                response: Response<List<BasicCategoryModel>>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    response.body()?.let { mAdatper.submitList(it) }
                } else {
                    context?.let { HelperClass.showErrorMsg("Error ${response.code()}", it) }
                }
            }

            override fun onFailure(call: Call<List<BasicCategoryModel>>, t: Throwable) {
                context?.let { HelperClass.showErrorMsg("Error ${t.localizedMessage}", it) }
            }

        })
    }

    override fun onItemSelected(position: Int, item: BasicCategoryModel) {
        // change the page to another place

    }
}