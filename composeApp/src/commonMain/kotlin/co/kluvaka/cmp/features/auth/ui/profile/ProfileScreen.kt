package co.kluvaka.cmp.features.auth.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import co.kluvaka.cmp.features.auth.ui.profile.ProfileOperation.Actions
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.auth_token_label
import kluvaka.composeapp.generated.resources.email_label
import kluvaka.composeapp.generated.resources.logout
import kluvaka.composeapp.generated.resources.profile_title
import kluvaka.composeapp.generated.resources.user_id_label
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ProfileViewModel>()
        val state by viewModel.state.collectAsState()
        
        ProfileScreenContent(
            state = state,
            onLogoutClick = { viewModel.handleAction(Actions.Logout) },
        )
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.user_id_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.userId ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = stringResource(Res.string.email_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.email ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = stringResource(Res.string.auth_token_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.authToken?.let { 
                        if (it.length > 50) "${it.take(50)}..." else it 
                    } ?: "N/A",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
        
        Button(
            onClick = onLogoutClick,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(
                text = if (state.isLoading) "Logging out..." else stringResource(Res.string.logout)
            )
        }
    }
}

