package com.joseluna.payments.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joseluna.payments.ui.theme.PaymentsTheme

enum class PaymentDialogState {
    Waiting,
    Processing,
    Success,
    Error;
}

@Composable
fun PaymentDialog(paymentState: PaymentDialogState,
                  quantity: String,
                  dismiss: () -> Unit,
                  onResult: () -> Unit,
){

    val icon = when(paymentState){
        PaymentDialogState.Waiting -> Icons.Filled.Info
        PaymentDialogState.Processing ->Icons.Filled.Face
        PaymentDialogState.Success -> Icons.Filled.CheckCircle
        PaymentDialogState.Error -> Icons.Filled.Close
    }

    val iconColor = when(paymentState){
        PaymentDialogState.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.secondary
    }

    val title = when(paymentState){
        PaymentDialogState.Waiting -> "Acerca la tarjeta"
        PaymentDialogState.Processing -> "Procesando Pago"
        PaymentDialogState.Success -> "Exito"
        PaymentDialogState.Error -> "Error"
    }

    val titleColor = when(paymentState){
        PaymentDialogState.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val subtitle = when(paymentState){
        PaymentDialogState.Waiting -> "Acerca la tarjeta al censor para procesar el pago de $$quantity"
        PaymentDialogState.Processing -> "Loading..."
        PaymentDialogState.Success -> "La transaccion por el monto de $$quantity fue procesada con exito"
        PaymentDialogState.Error -> "Hubo un error procesando el pago de $$quantity"
    }

    val subtitleColor = when(paymentState){
        PaymentDialogState.Error -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val backgroundColor = when(paymentState){
        PaymentDialogState.Error -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val buttonColor = when(paymentState){
        PaymentDialogState.Error -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    AlertDialog(
        iconContentColor = iconColor,
        containerColor = backgroundColor,
        icon = {
            Icon(icon,
                contentDescription = "test"
            )
        },
        title = {
            Text(text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = titleColor
            )
        },
        text ={
            if(paymentState == PaymentDialogState.Processing){
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }else{
                Text(text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )
            }
        },
        onDismissRequest = {
//            onDismiss()
        },
        confirmButton = {
            if(paymentState == PaymentDialogState.Success || paymentState == PaymentDialogState.Error){
                TextButton(onClick = onResult) {
                    Text(
                        text="Continuar",
                        color = buttonColor
                    )
                }
            }
        },
        dismissButton = {
            if(paymentState != PaymentDialogState.Success && paymentState != PaymentDialogState.Error){
                TextButton(
                    onClick = dismiss,
                    enabled = paymentState != PaymentDialogState.Processing
                ) {
                    Text(
                        text="Cancelar",
                        color = buttonColor
                    )
                }
            }
        }
    )

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDialogSuccess(){
    PaymentsTheme {
        PaymentDialog(paymentState = PaymentDialogState.Success, quantity = "100", dismiss = {}){}
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDialogSuccessDark(){
    PaymentsTheme {
        PaymentDialog(paymentState = PaymentDialogState.Success, quantity = "100", dismiss = {}){}
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDialog(){
    PaymentsTheme {
        PaymentDialog(paymentState = PaymentDialogState.Error, quantity = "100", dismiss = {}){}
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDialogDark(){
    PaymentsTheme {
        PaymentDialog(paymentState = PaymentDialogState.Error, quantity = "100", dismiss = {}){}
    }
}