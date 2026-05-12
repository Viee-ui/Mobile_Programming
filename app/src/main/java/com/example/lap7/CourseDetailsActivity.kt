@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION", "EXPERIMENTAL_API_USAGE", "OPT_IN_USAGE_ERROR")

package com.example.lap7

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.lap7.ui.theme.Lap7Theme
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lap7Theme {
                CourseDetailsScreen()
            }
        }
    }
}

@Composable
private fun CourseDetailsScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val courseList = remember { mutableStateListOf<Course>() }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    val loadCourses: () -> Unit = {
        isLoading = true
        hasError = false

        FirebaseFirestore.getInstance()
            .collection("Courses")
            .get()
            .addOnSuccessListener { snapshots ->
                isLoading = false
                courseList.clear()

                snapshots.documents.forEach { document ->
                    val course = document.toObject(Course::class.java)
                    if (course != null) {
                        course.courseID = document.id
                        courseList.add(course)
                    }
                }

                if (courseList.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.course_empty_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { error ->
                isLoading = false
                hasError = true
                Toast.makeText(
                    context,
                    context.getString(R.string.course_load_failed_toast, error.message.orEmpty()),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    LaunchedEffect(Unit) {
        loadCourses()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                loadCourses()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.course_details_title),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    },
                    actions = {
                        TextButton(onClick = loadCourses) {
                            Text(
                                text = stringResource(R.string.course_reload_action),
                                color = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F9D58)
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                ) {
                    Text(text = stringResource(R.string.course_add_action))
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.course_list_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                when {
                    isLoading -> LoadingState()
                    hasError -> MessageState(message = stringResource(R.string.course_error_message))
                    courseList.isEmpty() -> MessageState(message = stringResource(R.string.course_empty_message))
                    else -> CourseListContent(
                        courseList = courseList,
                        onCourseClick = { selectedCourse ->
                            val intent = Intent(context, UpdateCourse::class.java).apply {
                                putExtra("courseName", selectedCourse.courseName)
                                putExtra("courseDuration", selectedCourse.courseDuration)
                                putExtra("courseDescription", selectedCourse.courseDescription)
                                putExtra("courseID", selectedCourse.courseID)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = stringResource(R.string.course_loading_message))
    }
}

@Composable
private fun MessageState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CourseListContent(
    courseList: List<Course>,
    onCourseClick: (Course) -> Unit
) {
    LazyColumn {
        items(courseList, key = { it.courseID ?: it.hashCode().toString() }) { course ->
            CourseCard(course = course, onClick = { onCourseClick(course) })
        }
    }
}

@Composable
private fun CourseCard(course: Course, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = course.courseName.orEmpty(),
                color = Color(0xFF0F9D58),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.course_duration_label,
                    course.courseDuration.orEmpty()
                ),
                color = Color.Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.course_description_label,
                    course.courseDescription.orEmpty()
                ),
                color = Color.Black,
                fontSize = 15.sp
            )
        }
    }
}