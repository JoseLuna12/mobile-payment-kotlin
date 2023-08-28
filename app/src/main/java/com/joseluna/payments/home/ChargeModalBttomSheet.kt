package com.joseluna.payments.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseluna.payments.ui.theme.PaymentsTheme
import kotlinx.coroutines.launch

enum class ChargeActions(private val content: String) {
    One("1"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Zero("0"),
    Comma(","),
    Delete(""),
    Charge(""),
    Cancel("");

    fun getContent() = content
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargeModal(
    onDismiss: () -> Unit,
    state: SheetState,
    onChargeButton: (value: String) -> Unit
){

    val localHaptic = LocalHapticFeedback.current

    var quantity by remember {
        mutableStateOf("0,00")
    }
    val scope = rememberCoroutineScope()

    fun handleButtonsClicks(action: ChargeActions){
        val content = action.getContent()
        localHaptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        when(action){
            ChargeActions.Delete -> quantity = "0,00"
            ChargeActions.Charge -> {
                if(quantity == "0,00"){
                    return
                }
                onChargeButton(quantity)
            }
            ChargeActions.Cancel -> {
                quantity = "0,00"
                scope.launch {
                    state.hide()
                    onDismiss()
                }
            }
            else -> {
                if(quantity == "0,00"){

                    if(content == "0"){
                        return
                    }

                    quantity = content
                    return
                }

                if(quantity.contains(",")){

                    val decimal = quantity.split(",")
                    if(decimal[1].length < 2){
                        quantity += content
                    }
                    return
                }

                if(quantity.length < 6){
                    quantity += content
                }
            }
        }
    }


    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val windowInsets = if (edgeToEdgeEnabled)
        WindowInsets(0) else BottomSheetDefaults.windowInsets
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = state, windowInsets = windowInsets) {
        Column( verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text = "$$quantity",
                    style = MaterialTheme.typography.displayMedium
                )
            Divider(modifier= Modifier.padding(horizontal = 30.dp))
            }

            ButtonsArrangement{action ->
                handleButtonsClicks(action)
            }

            ActionSection{action ->
                handleButtonsClicks(action)
            }
        }
    }
}

@Composable
fun ButtonsArrangement(buttonsAction: (action: ChargeActions) -> Unit){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NumberButton(number = "1"){
                buttonsAction(ChargeActions.One)
            }
            NumberButton(number = "4"){
                buttonsAction(ChargeActions.Four)
            }
            NumberButton(number = "7"){
                buttonsAction(ChargeActions.Seven)
            }
            SpecialCharsButton(content = "•"){
                buttonsAction(ChargeActions.Comma)
            }
       }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NumberButton(number = "2"){
                buttonsAction(ChargeActions.Two)
            }
            NumberButton(number = "5"){
                buttonsAction(ChargeActions.Five)
            }
            NumberButton(number = "8"){
                buttonsAction(ChargeActions.Eight)
            }
            NumberButton(number = "0"){
                buttonsAction(ChargeActions.Zero)
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            NumberButton(number = "3"){
                buttonsAction(ChargeActions.Three)
            }
            NumberButton(number = "6"){
                buttonsAction(ChargeActions.Six)
            }
            NumberButton(number = "9"){
                buttonsAction(ChargeActions.Nine)
            }
            SpecialCharsButton(content = "Borrar"){
                buttonsAction(ChargeActions.Delete)
            }
        }
    }
}

@Composable
fun NumberButton(number: String, action: () -> Unit){
    OutlinedButton(
        onClick = action
    ) {
        Text(text = number,modifier = Modifier.padding(horizontal = 19.dp))
    }
}

@Composable
fun SpecialCharsButton(content: String, action: () -> Unit){
    TextButton(onClick = action) {
        Text(text = content)
    }
}
@Composable
fun ActionSection(action: (action: ChargeActions) -> Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 90.dp)
        .padding(bottom = 25.dp),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                      action(ChargeActions.Charge)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor =MaterialTheme.colorScheme.onSecondaryContainer
            )) {
            Text(text = "Cobrar")
        }
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                action(ChargeActions.Cancel)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor =MaterialTheme.colorScheme.primary
            )) {
            Text(text = "Cancelar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ModalView(){
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = SheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        initialValue = SheetValue.Expanded
    )
    PaymentsTheme {
        ChargeModal(onDismiss = {}, state = bottomSheetState){
            println(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode =  UI_MODE_NIGHT_YES)
@Composable
fun ModalViewDark(){
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = SheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        initialValue = SheetValue.Expanded
    )
    PaymentsTheme {
        ChargeModal(onDismiss = {}, state = bottomSheetState){
            println(it)
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ActionSectionPreview(){

    PaymentsTheme {
        ActionSection() {

        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun Buttons(){
    PaymentsTheme {
        Surface() {
            Column {
                NumberButton(number = "1") {

                }
                SpecialCharsButton(content = "Clear") {

                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ButtonsDark(){
    PaymentsTheme {
        Column {
            NumberButton(number = "1") {

            }
            SpecialCharsButton(content = "Clear") {

            }
        }
    }
}