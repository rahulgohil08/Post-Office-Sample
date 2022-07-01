package com.theworld.androidtemplatewithnavcomponents.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.theworld.androidtemplatewithnavcomponents.LoginActivity
import com.theworld.androidtemplatewithnavcomponents.R
import com.theworld.androidtemplatewithnavcomponents.adapters.PostOfficeAdapter
import com.theworld.androidtemplatewithnavcomponents.data.response.PostOffice
import com.theworld.androidtemplatewithnavcomponents.databinding.FragmentDashboardBinding
import com.theworld.androidtemplatewithnavcomponents.ui.PostOfficeViewModel
import com.theworld.androidtemplatewithnavcomponents.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    PostOfficeAdapter.PostOfficeInterface {

    companion object {
        private const val TAG = "DashboardFragment"
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostOfficeViewModel by viewModels()

    private val stringerAdapter by lazy { PostOfficeAdapter(this) }

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    /*----------------------------------------- On Create View -------------------------------*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater)
        return binding.root
    }

    /*----------------------------------------- On ViewCreated -------------------------------*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        init()
        collectData()

    }

    private fun collectData() {

        collectLatestFlow(viewModel.fetchPostOffice()) { resource ->

            binding.loadingSpinner.isVisible = resource is Resource.Loading

            when (resource) {

                is Resource.Success -> {
                    val data = resource.value.postOffice
                    stringerAdapter.submitList(data)

                    binding.notFound.isVisible = data.isEmpty()
                }
                is Resource.Failure -> {
                    handleApiError(resource)
                }

                else -> Unit
            }
        }


    }


    /*----------------------------------------- Init -------------------------------*/


    private fun init() {

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = stringerAdapter
        }


    }


    override fun onClick(postOffice: PostOffice) {
        // We Can send the ID but API missing
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToDetailsDialogFragment(postOffice)
        findNavController().navigate(action)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.logout) {
            confirmDialog(message = "Are you sure want to Logout?") {
                sharedPrefManager.clear()
                requireActivity().startNewActivity(LoginActivity::class.java)
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


}