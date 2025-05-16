package com.example.seekshop.ui.location

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel = hiltViewModel(),
    onLocationSelected: (String) -> Unit
) {
    var zipCode by remember { mutableStateOf("") }
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }
    val locationState by viewModel.locationState.collectAsState()

    Column(modifier = Modifier.padding(PaddingValues(16.dp))) {

        when (locationState) {
            is LocationState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LocationState.Success -> {
                val locations = (locationState as LocationState.Success).locations
                LazyColumn {
                    items(locations) { location ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    viewModel.saveLocation(location)
                                    onLocationSelected(location.id)
                                }
                        ) {
                            Column (
                                modifier = Modifier.padding(16.dp)
                            ){
                                Text(
                                    text = location.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                        text = location.address.street,
                                        style = MaterialTheme.typography.bodyLarge
                                )
                                Row {
                                    with(location.address) {
                                        Text(
                                            text = "$city, $state $zipCode",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is LocationState.Error -> {
                val message = (locationState as LocationState.Error).message
                Text("Error: $message")
            }

            else -> {

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    modifier = Modifier.padding(bottom = 8.dp),
                    label = { Text("Enter Zip Code") },
                )
                Button(
                    onClick = { viewModel.fetchUserLocation(zipCode) },
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    Text("Submit")
                }
                Button(
                    onClick = {
                        if (locationPermissionState.status.isGranted) {
                            viewModel.fetchNearbyStores()
                        } else {
                            locationPermissionState.launchPermissionRequest()
                        }
                    }
                ) {
                    Text("Use My Location")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationScreen{}
}