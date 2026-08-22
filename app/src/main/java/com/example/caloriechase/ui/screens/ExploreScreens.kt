package com.example.caloriechase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.AchievementItem
import com.example.caloriechase.ui.CoachTip
import com.example.caloriechase.ui.ProgressHighlight
import com.example.caloriechase.ui.TreasureSpot
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenScaffold
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonPurple
import com.example.caloriechase.ui.theme.SurfaceOutline

@Composable
fun TreasureScreen(
    treasures: List<TreasureSpot>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onPlanRoute: () -> Unit
) {
    ScreenScaffold(
        title = "Treasure",
        modifier = modifier.fillMaxSize(),
        onBack = onBack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SurfacePanel(emphasized = true) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Featured hunt",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText("The Bridge Crown route has the highest reward density in today's placeholder rotation.")
                    }
                }
            }
            items(treasures) { treasure ->
                SurfacePanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = treasure.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText(treasure.subtitle)
                        Text(
                            text = "${treasure.reward} • ${treasure.distanceAway}",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonOrange
                        )
                    }
                }
            }
            item {
                PrimaryButton(text = "Plan treasure route", onClick = onPlanRoute)
            }
        }
    }
}

@Composable
fun ProgressScreen(
    highlights: List<ProgressHighlight>,
    achievements: List<AchievementItem>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    ScreenScaffold(
        title = "Progress",
        modifier = modifier.fillMaxSize(),
        onBack = onBack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                        .border(1.dp, SurfaceOutline, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Weekly consistency chart placeholder",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonBlue
                    )
                }
            }
            items(highlights) { highlight ->
                SurfacePanel(emphasized = true) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = highlight.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = highlight.value,
                            style = MaterialTheme.typography.headlineSmall,
                            color = NeonGreen
                        )
                        BodyText(highlight.supporting)
                    }
                }
            }
            item {
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            items(achievements) { achievement ->
                SurfacePanel {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = achievement.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText(achievement.description)
                        Text(
                            text = if (achievement.unlocked) "Unlocked" else "Locked",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (achievement.unlocked) NeonGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoachScreen(
    tips: List<CoachTip>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onPlanRoute: () -> Unit
) {
    ScreenScaffold(
        title = "Coach",
        modifier = modifier.fillMaxSize(),
        onBack = onBack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                        .border(1.dp, SurfaceOutline, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            item {
                SurfacePanel(emphasized = true) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Today's recommendation",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText("A mid-distance jog route is the best placeholder match for recovery and streak protection today.")
                    }
                }
            }
            items(tips) { tip ->
                SurfacePanel {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = tip.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText(tip.message)
                    }
                }
            }
            item {
                PrimaryButton(text = "Generate route suggestion", onClick = onPlanRoute)
            }
        }
    }
}
