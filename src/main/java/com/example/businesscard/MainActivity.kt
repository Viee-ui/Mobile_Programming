package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme
import kotlinx.coroutines.delay

// ==================== MÀU SẮC - DỄ TÙY CHỈNH ====================
private val PrimaryGreen = Color(0xFF3DDC84)
private val LogoDark     = Color(0xFF073042)
private val CardBg       = Color(0xFFDAEDE3)
private val TextDark     = Color(0xFF1F1F1F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BusinessCardScreen()
                }
            }
        }
    }
}

@Composable
fun BusinessCardScreen() {
    var isVisible by remember { mutableStateOf(false) }

    // Khởi động animation khi màn hình load
    LaunchedEffect(Unit) {
        delay(100)           // delay nhẹ để animation mượt hơn
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + scaleIn(
            initialScale = 0.95f,
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(24.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp, horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AndroidLogoSection(isVisible)
                Spacer(modifier = Modifier.height(32.dp))
                PersonalInfoSection(isVisible)
                Spacer(modifier = Modifier.height(64.dp))
                ContactInfoSection(isVisible)
            }
        }
    }
}

// ==================== ẢNH TRÒN + ANIMATION SCALE + BOUNCE ====================
@Composable
private fun AndroidLogoSection(isVisible: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        modifier = Modifier
            .size(140.dp)
            .shadow(elevation = 12.dp, shape = CircleShape)
            .graphicsLayer { scaleX = scale; scaleY = scale },   // ← Animation scale
        shape = CircleShape,
        color = LogoDark,
        border = BorderStroke(6.dp, Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.android_logo),
            contentDescription = "Ảnh cá nhân",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun PersonalInfoSection(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800, delayMillis = 200))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Jennifer Doe",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Light,
                color = TextDark
            )
            Text(
                text = "Android Developer Extraordinaire",
                style = MaterialTheme.typography.titleMedium,
                color = PrimaryGreen,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ContactInfoSection(isVisible: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ContactRow(Icons.Filled.Call, "+84 345 3743 21", isVisible, delay = 300)
        ContactRow(Icons.Filled.Share, "@AndroidDev", isVisible, delay = 500)
        ContactRow(Icons.Filled.Email, "vietpn.24ite@vku.udn.vn", isVisible, delay = 700)
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    info: String,
    isVisible: Boolean,
    delay: Int
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(600, delayMillis = delay)
        ) + fadeIn(animationSpec = tween(600, delayMillis = delay))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.78f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = info,
                style = MaterialTheme.typography.bodyLarge,
                color = TextDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== PREVIEW ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BusinessCardScreen()
        }
    }
}