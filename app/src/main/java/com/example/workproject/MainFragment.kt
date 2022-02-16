package com.example.workproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.workproject.api.ApiService
import com.example.workproject.api.PostModel
import com.example.workproject.api.ServiceGenerator
import com.example.workproject.database.ActivityApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Response


class MainFragment : Fragment() {

    var navController: NavController? = null

    private var _binding: MainFragment? = null

    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by activityViewModels {
        ActivityViewModelFactory(
            (activity?.application as ActivityApplication).database
                .activityDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getPosts()

        fun getRequest(){
            call.clone().enqueue(object : retrofit2.Callback<PostModel> {
                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {
                    if (response.isSuccessful) {
                        Log.e("SUCCESS", response.body().toString())
                        Log.e("ACTIVITY", response.body()?.activity.toString() )
                        view.findViewById<TextView>(R.id.tvActivity).text = response.body()?.activity.toString()
                        view.findViewById<TextView>(R.id.tvType).text =response.body()?.type.toString()
                        view.findViewById<TextView>(R.id.tvParticipants).text=response.body()?.participants.toString()
                        view.findViewById<TextView>(R.id.tvPrice).text =  response.body()?.price.toString()

                        view.findViewById<FloatingActionButton>(R.id.save).setVisibility(View.VISIBLE)
                    }
                }

                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("error", t.message.toString())

                }

            })

        }
        //initiating get request
        view.findViewById<Button>(R.id.btnClick).setOnClickListener{
            getRequest()

        }

        //adding data to a database
        fun add(){
            Log.e("PARTICIPANTS",view.findViewById<TextView>(R.id.tvParticipants).text.toString() )
            viewModel.addNewItem(
                view.findViewById<TextView>(R.id.tvActivity).text.toString(),
                view.findViewById<TextView>(R.id.tvType).text.toString(),
                Integer.parseInt(view.findViewById<TextView>(R.id.tvParticipants).text.toString()),
                view.findViewById<TextView>(R.id.tvPrice).text.toString().toDouble()
            )

        }

        view.findViewById<FloatingActionButton>(R.id.save).setOnClickListener {
            add()
            //navController?.navigate(R.id.action_mainFragment_to_itemListFragment)
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToItemListFragment())

    }
    }


}

















