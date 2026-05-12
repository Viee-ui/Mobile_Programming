@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("DEPRECATION", "EXPERIMENTAL_API_USAGE", "OPT_IN_USAGE_ERROR")

package com.example.lap7

import android.content.Context
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lap7.ui.theme.Lap7Theme
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("courseName")
        val duration = intent.getStringExtra("courseDuration")
        val description = intent.getStringExtra("courseDescription")
        val courseID = intent.getStringExtra("courseID")

        if (courseID.isNullOrBlank()) {
            Toast.makeText(this, getString(R.string.update_missing_course_id), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            Lap7Theme {
                UpdateCourseScreen(
                    courseId = courseID,
                    name = name,
                    duration = duration,
                    description = description
                )
            }
        }
    }
}

@Composable
private fun UpdateCourseScreen(
    courseId: String,
    name: String?,
    duration: String?,
    description: String?
) {
    val context = LocalContext.current
    var courseName by remember { mutableStateOf(TextFieldValue(name.orEmpty())) }
    var courseDuration by remember { mutableStateOf(TextFieldValue(duration.orEmpty())) }
    var courseDescription by remember { mutableStateOf(TextFieldValue(description.orEmpty())) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.update_course_title),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F9D58)
                    )
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
                Text(text = stringResource(R.string.update_course_hint))

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = { Text(text = stringResource(R.string.main_course_name_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = courseDuration,
                    onValueChange = { courseDuration = it },
                    label = { Text(text = stringResource(R.string.main_course_duration_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = courseDescription,
                    onValueChange = { courseDescription = it },
                    label = { Text(text = stringResource(R.string.main_course_description_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        when {
                            courseName.text.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_name),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            courseDuration.text.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_duration),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            courseDescription.text.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.main_enter_course_description),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                updateDataToFirebase(
                                    courseID = courseId,
                                    name = courseName.text.trim(),
                                    duration = courseDuration.text.trim(),
                                    description = courseDescription.text.trim(),
                                    context = context
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.update_button))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(text = stringResource(R.string.delete_button))
                }

                if (showDeleteConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirm = false },
                        title = { Text(text = stringResource(R.string.delete_confirm_title)) },
                        text = { Text(text = stringResource(R.string.delete_confirm_message)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeleteConfirm = false
                                    deleteDataFromFirebase(courseID = courseId, context = context)
                                }
                            ) {
                                Text(text = stringResource(R.string.delete_confirm_action))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteConfirm = false }) {
                                Text(text = stringResource(R.string.delete_cancel_action))
                            }
                        }
                    )
                }
            }
        }
    }
}

fun updateDataToFirebase(
    courseID: String,
    name: String,
    duration: String,
    description: String,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("Courses")
        .document(courseID)
        .update(
            mapOf(
                "courseID" to courseID,
                "courseName" to name,
                "courseDuration" to duration,
                "courseDescription" to description
            )
        )
        .addOnSuccessListener {
            Toast.makeText(context, context.getString(R.string.update_success), Toast.LENGTH_SHORT).show()
            context.startActivity(
                Intent(context, CourseDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                context.getString(R.string.update_failed, it.message.orEmpty()),
                Toast.LENGTH_SHORT
            ).show()
        }
}

fun deleteDataFromFirebase(courseID: String, context: Context) {
    val db = FirebaseFirestore.getInstance()

    db.collection("Courses")
        .document(courseID)
        .delete()
        .addOnSuccessListener {
            Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
            context.startActivity(
                Intent(context, CourseDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                context.getString(R.string.delete_failed, it.message.orEmpty()),
                Toast.LENGTH_SHORT
            ).show()
        }
}