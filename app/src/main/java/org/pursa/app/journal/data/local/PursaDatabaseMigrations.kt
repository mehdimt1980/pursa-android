package org.pursa.app.journal.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object PursaDatabaseMigrations {
    val Migration1To2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `reflection_journal` (
                    `storyId` TEXT NOT NULL,
                    `contentRevision` INTEGER NOT NULL,
                    `reflectionStepId` TEXT NOT NULL,
                    `selectedReflectionOptionId` TEXT,
                    `revisitQuestionStepId` TEXT NOT NULL,
                    `completedAtEpochMillis` INTEGER NOT NULL,
                    `updatedAtEpochMillis` INTEGER NOT NULL,
                    `journalSchemaVersion` INTEGER NOT NULL,
                    PRIMARY KEY(`storyId`)
                )
                """.trimIndent(),
            )
        }
    }
}
