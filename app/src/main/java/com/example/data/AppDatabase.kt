package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        GKMainTopicEntity::class,
        GKSubTopicEntity::class,
        MCQQuestionEntity::class,
        RecentGKEntity::class,
        MegaQuizExamEntity::class,
        MegaQuizQuestionEntity::class,
        MegaQuizResultEntity::class,
        UniversityEntity::class,
        UniversityQuestionEntity::class,
        MCQPracticeProgressEntity::class,
        MCQQuizResultEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gkDao(): GKDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "admission_gk_database"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
