package org.pursa.app.journal

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.pursa.app.journal.data.local.PursaDatabaseMigrations
import org.pursa.app.progress.data.local.PursaDatabase

@RunWith(AndroidJUnit4::class)
class PursaDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        PursaDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory(),
    )

    @Test
    fun migrationFromOneToTwoCreatesReflectionJournalTable() {
        helper.createDatabase(DatabaseName, 1).close()

        val database = helper.runMigrationsAndValidate(
            DatabaseName,
            2,
            true,
            PursaDatabaseMigrations.Migration1To2,
        )

        database.query("SELECT COUNT(*) FROM reflection_journal").use { cursor ->
            cursor.moveToFirst()
            assertEquals(0, cursor.getInt(0))
        }
        database.close()
    }

    private companion object {
        const val DatabaseName = "pursa-migration-test"
    }
}
