package com.example.navigationcomponentexample.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.navigationcomponentexample.R

class SecondFragment : Fragment() {

    // Recibir argumentos usando Safe Args
    private val args: SecondFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Configurar el botón para navegar a ThirdFragment (grabadora)
        val btnGoToRecorder = view.findViewById<Button>(R.id.btnGoToRecorder)
        btnGoToRecorder.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
        }

        // Configurar el botón para navegar a CamaraFragment (cámara)
        val btnGoToCamera = view.findViewById<Button>(R.id.btnGoToCamera)
        btnGoToCamera.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_camaraFragment)
        }


        val btnGoToDatabase = view.findViewById<Button>(R.id.btnGoToDatabase)
        btnGoToDatabase.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_userCrudFragment)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }
}
