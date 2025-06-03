package com.coliseum.app.ui.screens.theatre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.coliseum.app.model.Auditorium
import com.coliseum.app.model.Theatre

@Composable
fun EditTheatreDialog(
    theatre: Theatre,
    theatreId: String? = null,
    isCreating: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (String?, Theatre) -> Unit
) {
    var name by remember { mutableStateOf(theatre.name) }
    var address by remember { mutableStateOf(theatre.address) }
    var city by remember { mutableStateOf(theatre.city) }
    var province by remember { mutableStateOf(theatre.province) }
    var postalCode by remember { mutableStateOf(theatre.postalCode) }
    var phoneNumber by remember { mutableStateOf(theatre.phoneNumber) }
    var capacity by remember { mutableStateOf(theatre.capacity.toString()) }
    var totalAuditoriums by remember { mutableStateOf(theatre.totalAuditoriums.toString()) }
    var note by remember { mutableStateOf(theatre.note) }
    var auditoriums by remember { mutableStateOf(theatre.auditoriums) }

    var nameError by remember { mutableStateOf(false) }
    var capacityError by remember { mutableStateOf(false) }
    var totalAuditoriumsError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = 700.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCreating) "Create New Theatre" else "Edit Theatre",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        if (!isCreating) {
                            name = it
                            nameError = false
                        }
                    },
                    label = { Text("Theatre Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    enabled = !isCreating,
                    supportingText = if (nameError) {
                        { Text("Theatre name is required") }
                    } else if (isCreating) {
                        { Text("Theatre name is set from the search") }
                    } else null
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Address Field
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // City and Province Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.weight(2f)
                    )

                    OutlinedTextField(
                        value = province,
                        onValueChange = { province = it },
                        label = { Text("Province") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Postal Code and Phone Number Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = postalCode,
                        onValueChange = { postalCode = it },
                        label = { Text("Postal Code") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Capacity and Total Auditoriums Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = {
                            capacity = it
                            capacityError = false
                        },
                        label = { Text("Capacity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = capacityError,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = totalAuditoriums,
                        onValueChange = {
                            totalAuditoriums = it
                            totalAuditoriumsError = false
                        },
                        label = { Text("Total Auditoriums") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = totalAuditoriumsError,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Auditoriums Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Auditoriums",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(
                                onClick = {
                                    auditoriums = auditoriums + Auditorium(
                                        number = auditoriums.size + 1,
                                        type = ""
                                    )
                                }
                            ) {
                                Text("Add")
                            }
                        }

                        auditoriums.forEachIndexed { index, auditorium ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = auditorium.number.toString(),
                                    onValueChange = { newNumber ->
                                        val number = newNumber.toIntOrNull() ?: 0
                                        auditoriums = auditoriums.toMutableList().apply {
                                            set(index, auditorium.copy(number = number))
                                        }
                                    },
                                    label = { Text("Number") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = auditorium.type,
                                    onValueChange = { newType ->
                                        auditoriums = auditoriums.toMutableList().apply {
                                            set(index, auditorium.copy(type = newType))
                                        }
                                    },
                                    label = { Text("Type") },
                                    placeholder = { Text("IMAX, UltraAVX, etc.") },
                                    modifier = Modifier.weight(2f)
                                )

                                IconButton(
                                    onClick = {
                                        auditoriums = auditoriums.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Note Field
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // Validate inputs
                            nameError = name.trim().isEmpty()
                            capacityError = capacity.trim().isNotEmpty() && capacity.toIntOrNull() == null
                            totalAuditoriumsError = totalAuditoriums.trim().isNotEmpty() && totalAuditoriums.toIntOrNull() == null

                            if (!nameError && !capacityError && !totalAuditoriumsError) {
                                val updatedTheatre = Theatre(
                                    id = theatreId ?: "",
                                    name = name.trim(),
                                    address = address.trim(),
                                    city = city.trim(),
                                    province = province.trim(),
                                    postalCode = postalCode.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    capacity = capacity.toIntOrNull() ?: 0,
                                    totalAuditoriums = totalAuditoriums.toIntOrNull() ?: 0,
                                    auditoriums = auditoriums,
                                    note = note.trim(),
                                    images = theatre.images
                                )

                                onSave(theatreId, updatedTheatre)
                            }
                        }
                    ) {
                        Text(if (isCreating) "Create" else "Save")
                    }
                }
            }
        }
    }
}