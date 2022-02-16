package com.example.workproject

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.workproject.database.ActivityApplication
import com.example.workproject.database.ActivityTable
import com.example.workproject.databinding.FragmentAddItemBinding
import kotlinx.android.synthetic.main.fragment_add_item.*


class AddItemFragment : Fragment() {
    private val viewModel: ActivityViewModel by activityViewModels {
        ActivityViewModelFactory(
            (activity?.application as ActivityApplication).database
                .activityDao()
        )
    }

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    lateinit var activity1: ActivityTable

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemActivity.text.toString(),
            binding.itemType.text.toString(),
            binding.itemParticipants.toString(),
            binding.itemPrice.toString()
        )
    }


    private fun bind(activity2: ActivityTable) {
        binding.apply {
            itemActivity.setText(activity2.activityType, TextView.BufferType.SPANNABLE)
            itemType.setText(activity2.type,TextView.BufferType.SPANNABLE)
            itemParticipants.setText(activity2.participants.toString(),TextView.BufferType.SPANNABLE)
            itemPrice.setText(activity2.price.toString(),TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }

        }
    }


    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.itemActivity.text.toString(),
                binding.itemType.text.toString(),
                binding.itemParticipants.text.toString().toInt(),
                binding.itemPrice.toString().toDouble()
            )
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            //this.arguments?.getInt("item_id")?.let {
                viewModel.updateItem(
                    //it,
                    this.navigationArgs.itemId,
                    this.binding.itemActivity.text.toString(),
                    this.binding.itemType.text.toString(),
                    this.binding.itemParticipants.text.toString().toInt(),
                    this.binding.itemPrice.text.toString().toDouble()

                )
            //}
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val id = navigationArgs.itemId
        val id = arguments?.getInt("item_id")
        val title = arguments?.getInt("title")
        Log.e("ID and TITLE", " "+id + " " +title)
        if (id != null) {
            if (id > 0) {
                viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                    activity1 = selectedItem
                    bind(activity1)
                }
            } else {
                binding.saveAction.setOnClickListener {
                    addNewItem()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}