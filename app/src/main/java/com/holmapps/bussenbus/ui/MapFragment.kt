package com.holmapps.bussenbus.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.holmapps.bussenbus.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint class MapFragment: Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    @Inject
    lateinit var viewModel: BusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MapFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MapFragmentBinding.bind(view)

        viewModel.fetchBusLocations()


    }
}