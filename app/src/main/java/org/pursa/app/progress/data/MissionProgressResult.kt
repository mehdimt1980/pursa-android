package org.pursa.app.progress.data

sealed interface MissionProgressResult<out T> {
    data class Success<T>(val value: T) : MissionProgressResult<T>
    data object Failure : MissionProgressResult<Nothing>
}
