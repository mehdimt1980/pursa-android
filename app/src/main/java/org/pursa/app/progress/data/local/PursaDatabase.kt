package org.pursa.app.progress.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.pursa.app.journal.data.local.ReflectionJournalDao
import org.pursa.app.journal.data.local.ReflectionJournalEntity

@Database(
    entities = [
        MissionProgressEntity::class,
        StorySessionEntity::class,
        StoryAnswerEntity::class,
        ReflectionJournalEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class PursaDatabase : RoomDatabase() {
    abstract fun missionProgressDao(): MissionProgressDao
    abstract fun reflectionJournalDao(): ReflectionJournalDao
}
