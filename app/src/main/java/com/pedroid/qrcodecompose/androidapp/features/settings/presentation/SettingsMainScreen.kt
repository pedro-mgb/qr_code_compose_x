package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.R

@Composable
fun SettingsMainScreen() {
    // TODO implement feature
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = R.string.bottom_navigation_item_settings))
        Spacer(modifier = Modifier.height(20.dp))
        Text(stringResource(id = R.string.to_be_implemented))
    }
}