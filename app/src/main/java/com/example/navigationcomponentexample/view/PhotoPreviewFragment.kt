package com.example.navigationcomponentexample.view

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentexample.databinding.FragmentPhotoPreviewBinding
import com.example.navigationcomponentexample.viewmodels.CameraViewModel
import java.io.File

class PhotoPreviewFragment : Fragment() {
    private var _binding: FragmentPhotoPreviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var cameraViewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraViewModel = ViewModelProvider(requireActivity())[CameraViewModel::class.java]

        updatePhotoPreview()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDeletePhoto.setOnClickListener {
            cameraViewModel.currentPhotoUri?.let { uri ->
                showDeleteConfirmationDialog(Uri.parse(uri))
            }
        }
    }

    private fun showDeleteConfirmationDialog(photoUri: Uri) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Foto")
            .setMessage("¿Estás seguro de que deseas eliminar esta foto?")
            .setPositiveButton("Eliminar") { _, _ ->
                deletePhoto(photoUri)
                updatePhotoPreview()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deletePhoto(photoUri: Uri) {
        val file = File(photoUri.path ?: "")
        if (file.exists() && file.delete()) {
            cameraViewModel.deleteCurrentPhoto()
            Toast.makeText(requireContext(), "Foto eliminada con éxito.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error al eliminar la foto.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePhotoPreview() {
        val currentUri = cameraViewModel.currentPhotoUri
        if (currentUri != null) {
            binding.ivFullPhoto.setImageURI(Uri.parse(currentUri))
        } else {
            Toast.makeText(requireContext(), "No hay más fotos para mostrar.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
