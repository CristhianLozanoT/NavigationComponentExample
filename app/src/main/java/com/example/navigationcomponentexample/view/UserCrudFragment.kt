package com.example.navigationcomponentexample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.navigationcomponentexample.UsuarioAdapter
import com.example.navigationcomponentexample.R
import com.example.navigationcomponentexample.entities.UserEntity
import com.example.navigationcomponentexample.providers.UsuarioDatabaseProvider
import com.example.navigationcomponentexample.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserCrudFragment : Fragment() {

    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_crud, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etApellido = view.findViewById<EditText>(R.id.etApellido)
        val btnRegistrar = view.findViewById<Button>(R.id.btnRegistrar)
        val btnEliminarTodos = view.findViewById<Button>(R.id.btnEliminarTodos)
        val lvUsuarios = view.findViewById<ListView>(R.id.lvUsuarios)

        // Configurar el adaptador para mostrar usuarios
        usuarioAdapter = UsuarioAdapter(requireContext(), mutableListOf())
        lvUsuarios.adapter = usuarioAdapter

        val userDao = UsuarioDatabaseProvider.getDatabase(requireContext()).getUserDao()

        // Cargar usuarios iniciales
        cargarUsuarios(userDao)

        // Registrar usuario
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()

            if (nombre.isNotBlank() && apellido.isNotBlank()) {
                val usuario = UserEntity(nombre = nombre, apellido = apellido)
                lifecycleScope.launch(Dispatchers.IO) {
                    userDao.insertar(usuario)
                    cargarUsuarios(userDao)
                }
                etNombre.text.clear()
                etApellido.text.clear()
            }
        }

        // Eliminar todos los usuarios
        btnEliminarTodos.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                userDao.eliminarTodos()
                cargarUsuarios(userDao)
            }
        }
    }

    private fun cargarUsuarios(userDao: UserDao) {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = userDao.getAllUsers()
            withContext(Dispatchers.Main) {
                usuarioAdapter.clear()
                usuarioAdapter.addAll(usuarios)
                usuarioAdapter.notifyDataSetChanged()
            }
        }
    }
}
