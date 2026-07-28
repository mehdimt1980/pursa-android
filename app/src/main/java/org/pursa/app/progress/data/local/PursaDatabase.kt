package org.pursa.app.progress.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MissionProgressEntity::class,
        StorySessionEntity::class,
        StoryAnswerEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class PursaDatabase : RoomDatabase() {
    abstract fun missionProgressDao(): MissionProgressDao
}
