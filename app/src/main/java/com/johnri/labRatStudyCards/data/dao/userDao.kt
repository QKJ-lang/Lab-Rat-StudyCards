package com.johnri.labRatStudyCards.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.johnri.labRatStudyCards.data.entity.userEntity

@Dao
interface userDao {

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): userEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: userEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): userEntity?

    //@Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    //suspend fun login(email: String, password: String): userEntity?

    @Update
    suspend fun updateUser(user: userEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}