package com.joseluna.payments.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.joseluna.payments.ui.theme.PaymentsTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun PaymentListView(
    modifier: Modifier = Modifier,
    viewModel: StateServiceScrollableList
){

    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state
    val pagerState = rememberPagerState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.loading,
        onRefresh = viewModel::loadToday
    )

    Column(modifier = modifier) {
        ScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
            state.content.forEachIndexed{index, row ->
                TabRowItem(
                    text = row.header,
                    selected = pagerState.currentPage == index
                ) {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.pullRefresh(pullRefreshState),
            count = state.content.count(),
            state= pagerState,
        ) {page ->
                LazyColumn{
                    items(state.content[page].content){
                        PaymentListItem(date = it.date, quantity = it.quantity)
                    }
                }

            if(page == 0){
                Box(modifier = Modifier.fillMaxWidth()){
                    PullRefreshIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        refreshing = state.loading,
                        state = pullRefreshState
                    )
                }
            }
        }
    }

}

@Composable
fun TabRowItem(text: String, selected: Boolean, onClick: () -> Unit){
    Tab(selected = selected,
        modifier = Modifier.height(55.dp),
        onClick = onClick
    ) {
        Text(text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentListItem(date: String, quantity: String){
    ListItem(
        overlineText = {
            Text(text = date)
        },
        text = {
            Text(text = "$$quantity")
        },
        icon = {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
//            Box(modifier = Modifier
//                .height(50.dp)
//                .width(50.dp)
//                .background(
//                    color = MaterialTheme.colorScheme.primary,
//                )
//            ) {
//
//            }
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PaymentListPreview(){
    PaymentsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val viewModel = viewModel<PaymentViewModel>()
            PaymentListView(viewModel = viewModel)
        }
    }
}