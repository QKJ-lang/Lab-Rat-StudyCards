package com.johnri.labRatStudyCards.data.repository

import com.johnri.labRatStudyCards.data.dao.userDao
import com.johnri.labRatStudyCards.data.entity.userEntity

class userRepository(private val userDao: userDao) {

    suspend fun register(user: userEntity): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserById(userId: Int): userEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun login(email: String, password: String): userEntity? {
        val user = userDao.getUserByEmail(email)

        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }

    suspend fun getUserByEmail(email: String): userEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: userEntity) {
        userDao.updateUser(user)
    }

    suspend fun registerUser(user: userEntity): Long {

        val existingUser = userDao.getUserByEmail(user.email)

        return if (existingUser == null) {
            userDao.insertUser(user)
        } else {
            -1
        }
    }

}