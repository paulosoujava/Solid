package com.paulo.loginapplication.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paulo.loginapplication.model.dto.UserDto

@Dao
interface UserDao {

    @Query("SELECT * FROM UserDto WHERE email=:email")
    suspend fun findByEmail(email: String): UserDto

    @Insert
    suspend fun insert(user: UserDto)

    @Query("SELECT EXISTS(SELECT * FROM UserDto WHERE email = :email)")
    suspend fun isRowIsExist(email: String): Boolean
}
