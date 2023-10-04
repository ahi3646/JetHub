package com.hasan.jetfasthub.screens.main.home.presentation.feeds

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.EmptyScreen
import com.hasan.jetfasthub.core.ui.components.ErrorScreen
import com.hasan.jetfasthub.core.ui.components.LoadingScreen
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedsScreen(
    contentPaddingValues: PaddingValues,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    events: LazyPagingItems<ReceivedEventsModel>,
    onNavigate: (Int, String?, String?) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = events.loadState) {
        if (events.loadState.refresh is LoadState.Error) {
            Toast.makeText(context, R.string.could_not_load_data, Toast.LENGTH_SHORT).show()
        }
    }
    when (events.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen()
        }

        is LoadState.NotLoading -> {
            if (events.itemSnapshotList.items.isEmpty())
                EmptyScreen(
                    message = stringResource(id = R.string.no_feeds),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = JetHubTheme.colors.background.primary)
                )
            else
                Content(
                    pullRefreshState = pullRefreshState,
                    contentPaddingValues = contentPaddingValues,
                    events = events,
                    onNavigate = onNavigate,
                    isRefreshing = isRefreshing
                )
        }

        is LoadState.Error -> {
            ErrorScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    pullRefreshState: PullRefreshState,
    contentPaddingValues: PaddingValues,
    events: LazyPagingItems<ReceivedEventsModel>,
    onNavigate: (Int, String?, String?) -> Unit,
    isRefreshing: Boolean
) {
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier
                .padding(contentPaddingValues)
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(events) { eventItem ->
                if (eventItem != null) {
                    FeedsItem(eventItem, onNavigate)
                }
            }
            item {
                if (events.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator()
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FeedsScreen_LightPreview() {
    JetFastHubTheme {
        FeedsScreen(
            contentPaddingValues = PaddingValues(all = JetHubTheme.dimens.spacing0),
            isRefreshing = false,
            pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
            events = flowOf<PagingData<ReceivedEventsModel>>(PagingData.empty()).collectAsLazyPagingItems(),
            onNavigate = { _, _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FeedsScreen_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        FeedsScreen(
            contentPaddingValues = PaddingValues(all = JetHubTheme.dimens.spacing0),
            isRefreshing = false,
            pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
            events = flowOf<PagingData<ReceivedEventsModel>>(PagingData.empty()).collectAsLazyPagingItems(),
            onNavigate = { _, _, _ -> }
        )
    }
}