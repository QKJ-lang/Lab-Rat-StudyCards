package com.johnri.labRatStudyCards.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.johnri.labRatStudyCards.data.dao.chapterDao
import com.johnri.labRatStudyCards.data.dao.deckDao
import com.johnri.labRatStudyCards.data.dao.statsDao
import com.johnri.labRatStudyCards.data.dao.studyCardDao
import com.johnri.labRatStudyCards.data.dao.templateDao
import com.johnri.labRatStudyCards.data.dao.userDao
import com.johnri.labRatStudyCards.data.entity.chapterEntity
import com.johnri.labRatStudyCards.data.entity.deckEntity
import com.johnri.labRatStudyCards.data.entity.statsEntity
import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import com.johnri.labRatStudyCards.data.entity.templateEntity
import com.johnri.labRatStudyCards.data.entity.userEntity

@Database(
    entities = [
        userEntity::class,
        deckEntity::class,
        chapterEntity::class,
        studyCardEntity::class,
        statsEntity::class,
        templateEntity::class
    ],
    version = 6
)
abstract class appDatabase : RoomDatabase() {

    //abstract val templateEntity: Any

    abstract fun userDao(): userDao
    abstract fun deckDao(): deckDao
    abstract fun chapterDao(): chapterDao
    abstract fun studyCardDao(): studyCardDao
    abstract fun statsDao(): statsDao
    abstract fun templateDao(): templateDao

    companion object {
        @Volatile
        private var INSTANCE: appDatabase? = null

        fun getDatabase(context: Context): appDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    appDatabase::class.java,
                    "labrat_db"
                )
                .fallbackToDestructiveMigration()
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}