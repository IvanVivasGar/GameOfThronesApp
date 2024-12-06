package edu.lasallebajio.gameofthronesapp.presentation.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.lasallebajio.gameofthronesapp.R
import edu.lasallebajio.gameofthronesapp.domain.entities.Question
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// QuestionBox.kt
@Composable
fun QuestionBox(
    question: Question,
    onOptionSelected: (String) -> Unit
) {
    var selectedOption by remember(question) { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(5.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFE0E0E0),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
            )

            AsyncImage(
                model = "http://68.183.30.211/img/questions/" + question.image,
                contentDescription = question.question,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                question.options.forEach { option ->
                    val buttonColor by animateColorAsState(
                        targetValue = when {
                            selectedOption != null && option == question.correctOption -> Color(0xFF2E7D32)
                            selectedOption != null && option == selectedOption && option != question.correctOption -> Color(0xFFC62828)
                            else -> Color(0xFF424242)
                        },
                        label = "buttonColor"
                    )

                    Button(
                        onClick = {
                            if (selectedOption == null) {
                                selectedOption = option
                                onOptionSelected(option)

                                // Add a coroutine to reset selection after a delay
                                coroutineScope.launch {
                                    delay(2000) // 2 seconds delay
                                    selectedOption = null
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(5.dp),
                        enabled = selectedOption == null
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}