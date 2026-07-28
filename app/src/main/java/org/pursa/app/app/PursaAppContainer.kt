package org.pursa.app.app

import android.content.Context
import androidx.room.Room
import org.pursa.app.content.data.AssetStoryDataSource
import org.pursa.app.content.data.LocalStoryContentRepository
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.journal.data.LocalReflectionJournalRepository
import org.pursa.app.journal.data.ReflectionJournalRepository
import org.pursa.app.journal.data.local.PursaDatabaseMigrations
import org.pursa.app.progress.data.LocalMissionProgressRepository
import org.pursa.app.progress.data.MissionProgressRepository
import org.pursa.app.progress.data.local.PursaDatabase

class PursaAppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: PursaDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            PursaDatabase::class.java,
            "pursa_progress.db",
        )
            .addMigrations(PursaDatabaseMigrations.Migration1To2)
            .build()
    }

    val storyContentRepository: StoryContentRepository by lazy {
        LocalStoryContentRepository(
            dataSource = AssetStoryDataSource(appContext.assets),
        )
    }

    val missionProgressRepository: MissionProgressRepository by lazy {
        LocalMissionProgressRepository(
            dao = database.missionProgressDao(),
        )
    }

    val reflectionJournalRepository: ReflectionJournalRepository by lazy {
        LocalReflectionJournalRepository(
            dao = database.reflectionJournalDao(),
        )
    }
}
