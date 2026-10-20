package com.example.c04_pa_exam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.c04_pa_exam.R
import com.example.c04_pa_exam.databinding.FragmentHomeBinding
import com.example.c04_pa_exam.ui.detail.DetailsFragment
import com.example.pa_mock.adapter.MovieAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAdapter(emptyList()) { movie ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailsFragment.newInstance(movie))
                .addToBackStack(null)
                .commit()
        }
        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = adapter

        // Search as you type
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearch(newText.orEmpty())
                return true
            }
        })

        // Sort chips
        binding.chipTitle.setOnClickListener { viewModel.onSortChipClicked(MovieSortField.TITLE) }
        binding.chipRating.setOnClickListener { viewModel.onSortChipClicked(MovieSortField.RATING) }

        viewModel.movies.observe(viewLifecycleOwner) { adapter.updateList(it) }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.onErrorShown()
            }
        }

        viewModel.sortField.observe(viewLifecycleOwner) { renderSortChips() }
        viewModel.sortOrder.observe(viewLifecycleOwner) { renderSortChips() }

        if (viewModel.movies.value == null) viewModel.loadAllMovies()
    }

    private fun renderSortChips() {
        val arrow = if (viewModel.sortOrder.value == SortOrder.DESC) "↓" else "↑"

        val titleSelected = viewModel.sortField.value == MovieSortField.TITLE
        binding.chipTitle.text = if (titleSelected) "Title $arrow" else "Title"
        binding.chipTitle.setChipBackgroundColorResource(if (titleSelected) R.color.button_blue else R.color.white)

        val ratingSelected = viewModel.sortField.value == MovieSortField.RATING
        binding.chipRating.text = if (ratingSelected) "Rating $arrow" else "Rating"
        binding.chipRating.setChipBackgroundColorResource(if (ratingSelected) R.color.button_blue else R.color.white)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}