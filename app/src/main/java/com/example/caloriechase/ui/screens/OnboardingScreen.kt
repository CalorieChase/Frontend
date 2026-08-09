package com.example.caloriechase.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.FeatureGlyph
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonPurple
import com.example.caloriechase.ui.theme.NeonRed
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accent: Color,
    val eyebrow: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Burn the calorie, chase the fat.",
            description = "CalorieChase helps you turn every run into a more guided, motivating experience.",
            icon = Icons.Rounded.DirectionsRun,
            accent = NeonOrange,
            eyebrow = "Welcome"
        ),
        OnboardingPage(
            title = "Discover hidden treasures while you run",
            description = "Add playful rewards and milestones to your routes so consistency feels exciting.",
            icon = Icons.Rounded.WorkspacePremium,
            accent = NeonPurple,
            eyebrow = "Treasure"
        ),
        OnboardingPage(
            title = "Watch your fitness goals come alive",
            description = "Keep steps, calories, and distance in view with progress that feels tangible.",
            icon = Icons.Rounded.MonitorHeart,
            accent = NeonRed,
            eyebrow = "Progress"
        ),
        OnboardingPage(
            title = "Use AI to find a track",
            description = "Discover routes that fit your energy, your surroundings, and the workout you want today.",
            icon = Icons.Rounded.AutoAwesome,
            accent = NeonBlue,
            eyebrow = "AI track"
        ),
        OnboardingPage(
            title = "You're ready to start your next run.",
            description = "Jump into a personalized frontend experience built around your routes, rhythm, and rewards.",
            icon = Icons.Rounded.Flag,
            accent = NeonGreen,
            eyebrow = "Get started"
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.lastIndex

    ScreenColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 24.dp,
            top = 16.dp,
            end = 24.dp,
            bottom = 24.dp
        )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) { page ->
            val item = pages[page]
            SurfacePanel(
                emphasized = page == pages.lastIndex,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = item.eyebrow,
                        style = MaterialTheme.typography.labelLarge,
                        color = item.accent
                    )
                    FeatureGlyph(icon = item.icon, accent = item.accent)
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    BodyText(item.description)
                    if (page == pages.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(
                                    color = item.accent.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = item.accent.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "Get Started",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = item.accent
                                )
                                Text(
                                    text = "Frontend preview ready",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val active = index == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (active) 24.dp else 8.dp, 8.dp)
                        .background(
                            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                )
            }
        }

        if (isLastPage) {
            PrimaryButton(text = "Get Started", onClick = onGetStarted)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(
                    text = "Next",
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
                SecondaryButton(
                    text = "Skip intro",
                    onClick = onGetStarted
                )
            }
        }
    }
}
