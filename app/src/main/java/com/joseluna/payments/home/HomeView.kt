package com.joseluna.payments.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseluna.payments.ui.theme.PaymentsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {

    var showPaymentModal by rememberSaveable {
        mutableStateOf(false)
    }
    var paymentModalState by rememberSaveable {
        mutableStateOf(PaymentDialogState.Success)
    }
    var quantity by rememberSaveable {
        mutableStateOf("0")
    }

    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = SheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        initialValue = SheetValue.Hidden,
    )
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Hola Usuario",
                    style = MaterialTheme.typography.headlineMedium,
                )
            })
        },
        floatingActionButton = {
            LargeFloatingActionButton(onClick = {
                scope.launch {
                    openBottomSheet = true
                    bottomSheetState.show()
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add icon")
            }
        }) {

        PaymentListView<PaymentViewModel>(modifier = Modifier.padding(it))

        if (openBottomSheet) {
            ChargeModal(onDismiss = {
                scope.launch {
                    openBottomSheet = false
                }
            }, state = bottomSheetState) { paymentQuantity ->

                quantity = paymentQuantity
                showPaymentModal = true
                openBottomSheet = false
            }
        }
        if (showPaymentModal) {
            PaymentDialog(
                paymentState = paymentModalState,
                quantity = quantity,
                dismiss = {
                    paymentModalState = PaymentDialogState.Waiting
                    showPaymentModal = false
                }
            ) {
                paymentModalState = PaymentDialogState.Waiting
                val newPaymentProcessed = PaymentItem(date = "31", quantity = quantity)

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun previewHome() {
    PaymentsTheme {
        HomeView()
    }
}