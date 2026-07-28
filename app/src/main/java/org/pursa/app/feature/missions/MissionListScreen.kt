package org.pursa.app.feature.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import org.pursa.app.R
import org.pursa.app.content.model.PursaStorySummary
import org.pursa.app.core.ui.PursaTestTags
import org.pursa.app.designsystem.component.PursaButton
import org.pursa.app.designsystem.component.PursaButtonVariant
import org.pursa.app.designsystem.component.PursaCard
import org.pursa.app.designsystem.component.PursaMessage
import org.pursa.app.designsystem.component.PursaMessageVariant
import org.pursa.app.designsystem.theme.PursaTheme
import org.pursa.app.progress.model.MissionProgressStatus

@Composable
fun MissionListScreen(
    state: MissionListUiState,
    onMissionClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(PursaTestTags.MissionListRoot),
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        Text(
            text = stringResource(R.string.missions_heading),
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        when (state) {
            MissionListUiState.Loading -> PursaMessage(
                title = stringResource(R.string.missions_heading),
                message = stringResource(R.string.missions_loading),
            )
            is MissionListUiState.Success -> MissionListSuccess(
                missions = state.missions,
                onMissionClick = onMissionClick,
            )
            MissionListUiState.InvalidContent -> MissionListError(
                title = stringResource(R.string.story_invalid_title),
                message = stringResource(R.string.story_invalid_message),
                onRetry = onRetry,
            )
            MissionListUiState.ReadFailure -> MissionListError(
                title = stringResource(R.string.story_read_failure_title),
                message = stringResource(R.string.story_read_failure_message),
                onRetry = onRetry,
            )
        }
    }
}

@Composable
private fun MissionListSuccess(
    missions: List<MissionListItemUiModel>,
    onMissionClick: (String) -> Unit,
) {
    if (missions.isEmpty()) {
        PursaMessage(
            title = stringResource(R.string.missions_empty_title),
            message = stringResource(R.string.missions_empty_message),
            variant = PursaMessageVariant.Empty,
        )
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.medium),
    ) {
        missions.forEach { item ->
            val mission = item.summary
            val statusColor = item.status.statusColor()
            PursaCard(
                title = mission.title,
                supportingText = mission.summary,
                onClick = { onMissionClick(mission.id) },
                accentColor = statusColor,
                containerColor = MaterialTheme.colorScheme.surface,
                borderColor = statusColor.copy(alpha = 0.42f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(mission.testTag),
                trailingContent = {
                    MissionProgressLabel(
                        status = item.status,
                        minAge = mission.recommendedMinAge,
                        maxAge = mission.recommendedMaxAge,
                        durationMinutes = mission.estimatedDurationMinutes,
                        storyId = mission.id,
                    )
                },
            )
        }
    }
}

@Composable
private fun MissionProgressLabel(
    status: MissionProgressStatus,
    minAge: Int,
    maxAge: Int,
    durationMinutes: Int,
    storyId: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PursaTheme.spacing.extraSmall),
    ) {
        Text(
            text = stringResource(
                R.string.mission_age_duration,
                minAge,
                maxAge,
                durationMinutes,
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(status.labelResId),
            modifier = Modifier.testTag(PursaTestTags.missionStatus(storyId)),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(status.actionResId),
            modifier = Modifier.testTag(PursaTestTags.missionAction(storyId)),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun MissionProgressStatus.statusColor() = when (this) {
    MissionProgressStatus.NotStarted -> PursaTheme.semanticColors.outlineStrong
    MissionProgressStatus.InProgress -> PursaTheme.semanticColors.info
    MissionProgressStatus.Completed -> PursaTheme.semanticColors.success
}

@Composable
private fun MissionListError(
    title: String,
    message: String,
    onRetry: () -> Unit,
) {
    PursaMessage(
        title = title,
        message = message,
        variant = PursaMessageVariant.Warning,
        action = {
            PursaButton(
                text = stringResource(R.string.story_retry),
                onClick = onRetry,
                variant = PursaButtonVariant.Secondary,
            )
        },
    )
}

private val PursaStorySummary.testTag: String
    get() = when (id) {
        "truth_broken_vase" -> PursaTestTags.MissionTruthBrokenVase
        else -> PursaTestTags.mission(id)
    }

private val MissionProgressStatus.labelResId: Int
    get() = when (this) {
        MissionProgressStatus.NotStarted -> R.string.mission_status_not_started
        MissionProgressStatus.InProgress -> R.string.mission_status_in_progress
        MissionProgressStatus.Completed -> R.string.mission_status_completed
    }

private val MissionProgressStatus.actionResId: Int
    get() = when (this) {
        MissionProgressStatus.NotStarted -> R.string.mission_action_start
        MissionProgressStatus.InProgress -> R.string.mission_action_continue
        MissionProgressStatus.Completed -> R.string.mission_action_replay
    }
