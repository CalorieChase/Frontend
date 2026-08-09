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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.WorkspacePremium
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
import com.example.caloriechase.ui.components.ScreenHeader
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
    onPlanRoute: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenHeader(
                title = "Treasure board",
                subtitle = "Ported from the legacy treasure surface using collectible placeholders."
            )
        }
        item {
            SurfacePanel(emphasized = true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Featured hunt",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText("The Bridge Crown route has the highest reward density in today’s placeholder rotation.")
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

@Composable
fun ProgressScreen(
    highlights: List<ProgressHighlight>,
    achievements: List<AchievementItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenHeader(
                title = "Progress",
                subtitle = "A fuller Compose version of the legacy progress screen with weekly summaries and achievements."
            )
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

@Composable
fun CoachScreen(
    tips: List<CoachTip>,
    modifier: Modifier = Modifier,
    onPlanRoute: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenHeader(
                title = "AI coach",
                subtitle = "This replaces the old AI fragment with a more polished coaching and route guidance surface."
            )
        }
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

@Composable
fun Map3DPreviewScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ScreenHeader(
                title = "3D map preview",
                subtitle = "A frontend-only stand-in for the old experimental map controls.",
                onBack = onBack
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                    .border(1.dp, SurfaceOutline, RoundedCornerShape(28.dp))
            ) {
                listOf(
                    Triple(Icons.Rounded.Map, "Map style", NeonBlue),
                    Triple(Icons.Rounded.WorkspacePremium, "3D tilt", NeonOrange),
                    Triple(Icons.Rounded.LockOpen, "Compass", NeonGreen)
                ).forEachIndexed { index, item ->
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 24.dp + (index * 90).dp, top = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(item.third.copy(alpha = 0.14f), CircleShape)
                                .border(1.dp, item.third.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.first, contentDescription = null, tint = item.third)
                        }
                        Text(item.second, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        item {
            SurfacePanel {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Preview notes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText("This screen is intentionally placeholder-only until live Maps integration is brought over.")
                }
            }
        }
    }
}
