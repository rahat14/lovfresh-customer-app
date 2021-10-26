package com.lovfreshuser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lovfreshuser.databinding.FragmentFoodMenuListBinding


class FoodMenuListFragment : Fragment() {
    private lateinit var binding: FragmentFoodMenuListBinding

    companion object {
        fun instance(hashTagsCode: Int): FoodMenuListFragment {
            val data = Bundle()
            data.putInt("hash_tag_codes", hashTagsCode)
            return FoodMenuListFragment().apply {
                arguments = data
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodMenuListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("has_tag_codes", 0) ?: 0
    }
    //   (PopularFragment.intsance(doc.code, "#${doc.name}", 10),doc.name)
}