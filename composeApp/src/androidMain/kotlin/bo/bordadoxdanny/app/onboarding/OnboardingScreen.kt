package bo.bordadoxdanny.app.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val slides by viewModel.slides.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    
    val lang = remember {
        Locale.getDefault().language.let { if (it in listOf("es", "en", "fr")) it else "en" }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSlides()
    }

    if (slides.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentSlide = slides[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F7FF))
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.previous() },
                enabled = !viewModel.isFirstSlide,
                modifier = Modifier.alpha(if (viewModel.isFirstSlide) 0f else 1f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            TextButton(onClick = onSkip) {
                Text(
                    text = when (lang) {
                        "es" -> "Omitir"
                        "fr" -> "Ignorer"
                        else -> "Skip"
                    },
                    color = Color(0xFF1565C0)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Center Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = currentSlide.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title
        Text(
            text = currentSlide.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = currentSlide.description,
            fontSize = 14.sp,
            color = Color(0xFF555577),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            slides.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentIndex) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (index == currentIndex) Color(0xFF1565C0) else Color(0xFFBBCCE8))
                )
                if (index < slides.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom Button
        Button(
            onClick = {
                if (viewModel.isLastSlide) {
                    viewModel.setCompleted()
                    onFinish()
                } else {
                    viewModel.next()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text(
                text = if (viewModel.isLastSlide) {
                    when (lang) {
                        "es" -> "Iniciar"
                        "fr" -> "Commencer"
                        else -> "Start"
                    }
                } else {
                    when (lang) {
                        "es" -> "Siguiente →"
                        "fr" -> "Suivant →"
                        else -> "Next →"
                    }
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
