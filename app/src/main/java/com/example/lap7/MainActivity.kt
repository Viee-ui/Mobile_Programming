@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION", "EXPERIMENTAL_API_USAGE", "OPT_IN_USAGE_ERROR")

package com.example.lap7

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lap7.ui.theme.Lap7Theme
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lap7Theme {
                AddCourseScreen()
            }
        }
    }
}

@Composable
private fun AddCourseScreen() {
    val context = LocalContext.current
    var courseName by remember { mutableStateOf("") }
    var courseDuration by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.main_add_course_title),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F9D58))
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.main_add_course_hint))

                Spacer(modifier = Modifier.height(12.dp))

                CourseInputField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = stringResource(R.string.main_course_name_label)
                )

                Spacer(modifier = Modifier.height(12.dp))

                CourseInputField(
                    value = courseDuration,
                    onValueChange = { courseDuration = it },
                    label = stringResource(R.string.main_course_duration_label)
                )

                Spacer(modifier = Modifier.height(12.dp))

                CourseInputField(
                    value = courseDescription,
                    onValueChange = { courseDescription = it },
                    label = stringResource(R.string.main_course_description_label)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        when {
                            courseName.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_name),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            courseDuration.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_duration),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            courseDescription.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_description),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                addDataToFirebase(
                                    courseName = courseName.trim(),
                                    courseDuration = courseDuration.trim(),
                                    courseDescription = courseDescription.trim(),
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.main_add_success),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        courseName = ""
                                        courseDuration = ""
                                        courseDescription = ""
                                    },
                                    onFailure = { exception ->
                                        Toast.makeText(
                                            context,
                                            context.getString(
                                                R.string.main_add_failed,
                                                exception.message.orEmpty()
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.main_add_button))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        context.startActivity(Intent(context, CourseDetailsActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.main_view_courses_button))
                }
            }
        }
    }
}

@Composable
private fun CourseInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

fun addDataToFirebase(
    courseName: String,
    courseDuration: String,
    courseDescription: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCourses: CollectionReference = db.collection("Courses")
    val courseID = dbCourses.document().id

    val course = Course(courseID, courseName, courseDuration, courseDescription)

    dbCourses.document(courseID)
        .set(course)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}
