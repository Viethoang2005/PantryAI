
package com.example.pantryai.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pantryai.R
import com.example.pantryai.ui.theme.PantryCream
import com.example.pantryai.ui.theme.PantryGreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2000.milliseconds)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PantryCream),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() +
                    scaleIn(initialScale = 0.8f) +
                    slideInVertically(
                        initialOffsetY = { it / 8 }
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.app_icon
                    ),
                    contentDescription = "Logo PantryAI",
                    modifier = Modifier.size(140.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "PantryAI",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = PantryGreen
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Nấu ăn ngon hơn mỗi ngày",
                    fontSize = 16.sp,
                    color = Color(0xFF78847B)
                )

                Spacer(Modifier.height(32.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = PantryGreen,
                    strokeWidth = 3.dp
                )
            }
        }

        Text(
            text = "COOK SMART • EAT WELL",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF78847B)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}
