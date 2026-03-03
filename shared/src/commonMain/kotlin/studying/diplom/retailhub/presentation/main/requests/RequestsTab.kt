package studying.diplom.retailhub.presentation.main.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_list

object RequestsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Заявки"
            val icon = painterResource(Res.drawable.ic_list)
            return TabOptions(index = 0u, title = title, icon = icon)
        }

    @Composable
    override fun Content() {
        val screenModel: RequestsViewModel = RequestsTab.koinScreenModel()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.loadRequests()
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(state.requests) { request ->
                        Text(
                            text = "Заявка: ${request.id}\nСтатус: ${request.status}",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }
        }
    }
}
