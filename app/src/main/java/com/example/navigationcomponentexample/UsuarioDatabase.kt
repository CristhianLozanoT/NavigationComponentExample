package com.example.navigationcomponentexample

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navigationcomponentexample.dao.UserDao
import com.example.navigationcomponentexample.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UsuarioDatabase: RoomDatabase() {
    abstract fun getUserDao():UserDao
}