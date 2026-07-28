package org.pursa.app.core.time

interface PursaClock {
    fun nowEpochMillis(): Long
}

object SystemPursaClock : PursaClock {
    override fun nowEpochMillis(): Long = System.currentTimeMillis()
}
