package com.example.c04_pa_exam.ui.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.c04_pa_exam.R
import com.example.c04_pa_exam.data.model.Movie
import com.example.c04_pa_exam.databinding.FragmentDetailsBinding
import com.example.c04_pa_exam.databinding.FragmentHomeBinding
import com.example.pa_mock.ui.detail.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movie.observe(viewLifecycleOwner) { m ->
            binding.tvTitle.text = m.title
            binding.tvRating.text = m.rating
            binding.tvDirector.text = m.director
            binding.tvReleaseDate.text = m.release_date
            binding.tvLength.text = "${m.length} min"
            binding.tvOverview.text = m.overview
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.onErrorShown()
            }
        }
        if (viewModel.movie.value == null) {
            viewModel.loadMovieById(requireArguments().getInt(ARG_ID))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(movie: Movie) = DetailsFragment().apply {
            arguments = Bundle().apply { putInt(ARG_ID, movie.id) }
        }
    }
}