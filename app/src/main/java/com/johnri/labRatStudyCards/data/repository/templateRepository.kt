package com.johnri.labRatStudyCards.data.repository

import com.johnri.labRatStudyCards.data.dao.templateDao
import com.johnri.labRatStudyCards.data.entity.templateEntity
import kotlinx.coroutines.flow.Flow

class templateRepository(private val templateDao: templateDao) {

    fun getTemplates(): Flow<List<templateEntity>> {
        return templateDao.getAllTemplates()
    }

    suspend fun insertTemplate(template: templateEntity) {
        templateDao.insertTemplate(template)
    }

    suspend fun deleteTemplate(template: templateEntity) {
        templateDao.deleteTemplate(template)
    }
}