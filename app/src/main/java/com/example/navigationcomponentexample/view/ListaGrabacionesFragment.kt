package com.example.navigationcomponentexample.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.navigationcomponentexample.R
import java.io.IOException

class ListaGrabacionesFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var listViewGrabaciones: ListView
    private val recordingList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_grabaciones, container, false)
        listViewGrabaciones = view.findViewById(R.id.listViewGrabaciones)
        loadRecordings()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, recordingList.map { "Grabación ${recordingList.indexOf(it) + 1}" })
        listViewGrabaciones.adapter = adapter

        listViewGrabaciones.setOnItemClickListener { _, _, position, _ ->
            playRecording(recordingList[position])
        }

        return view
    }

    private fun loadRecordings() {
        val recordingsDir = requireContext().externalCacheDir
        if (recordingsDir != null) {
            recordingList.clear()
            recordingsDir.listFiles()?.forEach { file ->
                if (file.extension == "mp3") {
                    recordingList.add(file.absolutePath)
                }
            }
        }
    }

    private fun playRecording(ruta: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(ruta)
                prepare()
                start()
                Toast.makeText(requireContext(), "Reproduciendo grabación", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al reproducir la grabación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
