package com.example.navigationcomponentexample.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentexample.R
import java.io.File
import java.io.IOException

class ThirdFragment : Fragment() {

    private var grabadora: MediaRecorder? = null
    private var ruta: String? = null
    private var isRecording = false
    private var handler = Handler()
    private var startTime = 0L
    private lateinit var btnGrabar: ImageView
    private lateinit var btnCancelar: Button
    private lateinit var btnVerGrabaciones: Button
    private lateinit var statusText: TextView
    private lateinit var timerText: TextView
    private var recordingCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        btnGrabar = view.findViewById(R.id.btnGrabar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
        btnVerGrabaciones = view.findViewById(R.id.btnVerGrabaciones)
        statusText = view.findViewById(R.id.statusText)
        timerText = view.findViewById(R.id.timerText)

        btnCancelar.visibility = View.GONE
        timerText.visibility = View.GONE

        btnGrabar.setOnClickListener { toggleRecording() }
        btnCancelar.setOnClickListener { cancelRecording() }
        btnVerGrabaciones.setOnClickListener {
            findNavController().navigate(R.id.action_thirdFragment_to_listaGrabacionesFragment)
        }

        checkPermissions()
        return view
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 0)
        }
    }

    private fun toggleRecording() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        recordingCount++
        ruta = "${requireContext().externalCacheDir?.absolutePath}/grabacion_$recordingCount.mp3"
        grabadora = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(ruta)
            try {
                prepare()
                start()
                isRecording = true
                startTime = System.currentTimeMillis()
                handler.post(updateTimer)

                btnCancelar.visibility = View.VISIBLE
                timerText.visibility = View.VISIBLE
                statusText.text = "Grabando..."
                Toast.makeText(requireContext(), "Grabación iniciada", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al iniciar la grabación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        grabadora?.apply {
            stop()
            release()
        }
        grabadora = null
        isRecording = false
        handler.removeCallbacks(updateTimer)

        btnCancelar.visibility = View.GONE
        timerText.visibility = View.GONE

        statusText.text = "Grabación guardada en: $ruta"
        Toast.makeText(requireContext(), "Grabación detenida y guardada", Toast.LENGTH_SHORT).show()
    }

    private fun cancelRecording() {
        if (isRecording) {
            grabadora?.apply {
                stop()
                release()
            }
            grabadora = null
            isRecording = false
            handler.removeCallbacks(updateTimer)
            ruta?.let { File(it).delete() }

            btnCancelar.visibility = View.GONE
            timerText.visibility = View.GONE

            statusText.text = "Grabación cancelada"
            timerText.text = "00:00"
            Toast.makeText(requireContext(), "Grabación cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private val updateTimer = object : Runnable {
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - startTime
            val seconds = (elapsedTime / 1000).toInt() % 60
            val minutes = (elapsedTime / (1000 * 60) % 60).toInt()
            timerText.text = String.format("%02d:%02d", minutes, seconds)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            stopRecording()
        }
    }
}
