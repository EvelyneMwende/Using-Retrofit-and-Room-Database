package com.example.workproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.workproject.database.ActivityApplication
import com.example.workproject.database.ActivityTable
import com.example.workproject.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ItemDetailFragment : Fragment() {


    private val navigationArgs: ItemDetailFragmentArgs by navArgs()
    lateinit var activity1: ActivityTable
    var navController: NavController? = null


    private val viewModel: ActivityViewModel by activityViewModels {
        ActivityViewModelFactory(
            (activity?.application as ActivityApplication).database.activityDao()
        )
    }

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun bind(activity: ActivityTable) {
        binding.apply {
            itemActivity.text = activity.activityType
            itemType.text=activity.type
            itemParticipants.text=activity.participants.toString()
            itemPrice.text = activity.price.toString()
           deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener {editItem() }
        }
    }

    private fun editItem() {
//        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
//            getString(R.string.edit_fragment_title),
//            activity1.id
//        )

//        this.findNavController().navigate(action)

        val bundle = bundleOf("title" to "Edit item",
            "item_id" to activity1.id)
        findNavController().navigate(R.id.action_itemDetailFragment_to_addItemFragment,bundle)

    }


    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you sure you want to delete ")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deleteItem()
            }
            .show()
    }



    private fun deleteItem() {
        viewModel.deleteItem(activity1)
        findNavController().navigateUp()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        //val id = arguments?.getInt("item_id")
        //if (id != null) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                activity1 = selectedItem
                bind(activity1)
            }
        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}