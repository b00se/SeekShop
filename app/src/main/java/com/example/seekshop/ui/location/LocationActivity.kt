package com.example.seekshop.ui.location

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationActivity : ComponentActivity() {

    private val locationViewModel: LocationViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationViewModel.fetchLatLong()
            } else {
               Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            locationViewModel.permissionRequestEvent.collect {event ->
                when(event) {
                    PermissionRequestEvent.Request -> {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    else -> {
                        //do nothing

                    }
                }
            }
        }

        setContent {
            LocationScreen()
        }
    }
}


@Composable
fun LocationScreen(locationViewModel: LocationViewModel = viewModel()) {
    var zipCode by remember { mutableStateOf("") }
    val locationState by locationViewModel.locationState.collectAsState()

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
                    onClick = { locationViewModel.fetchUserLocation(zipCode) },
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    Text("Submit")
                }
                Button(
                    onClick = { locationViewModel.requestLocationPermission() }
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
    LocationScreen()
}