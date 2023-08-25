package com.joseluna.payments.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.random.Random

data class PaymentItem(val date: String, val quantity: String)

data class PaymentContent(val header: String, var content: List<PaymentItem>){
    fun updateContent(newContent: List<PaymentItem>){
        this.content = newContent
    }
}

data class PaymentScreenState(
    val version: String = "0.0",
    val loading: Boolean = false,
    val content: List<PaymentContent> = emptyList()
)

abstract class StateServiceScrollableList: ViewModel(){
    abstract var _state: MutableState<PaymentScreenState>
    abstract var state: State<PaymentScreenState>
    abstract fun initItems()
    abstract fun loadToday()
    abstract fun newPayment(new: PaymentItem)
}

class PaymentService(){
    fun getContent(days: Int = 10): List<PaymentContent>{
        val content = arrayListOf<PaymentContent>()

        for (i in 0..days){
            if(i == 0){
                content.add(
                    PaymentContent(header = "Hoy", content = getDaysFrom("31"))
                )
                continue
            }
            if(i == 1){
                content.add(
                    PaymentContent(header = "Ayer", content = getDaysFrom("30"))
                )
                continue
            }
            else{
                content.add(
                    PaymentContent(header = "agosto ${31 - i}", content = getDaysFrom("${31 - i}"))
                )
            }
        }

        return content
    }

    fun getTodayUpdate(): List<PaymentItem>{
        return getDaysFrom("31")
    }

    private fun getDaysFrom(day: String): List<PaymentItem>{
        val payItems = arrayListOf<PaymentItem>()
        for(i in 0..100){
            val quantity = Random.nextInt(10, 6000)
            payItems.add(
                PaymentItem(
                    quantity = quantity.toString(),
                    date = "$day agosto"
                )
            )
        }
        return payItems
    }
}

class PaymentViewModel() : StateServiceScrollableList() {
    override var _state: MutableState<PaymentScreenState> = mutableStateOf(PaymentScreenState())
    override var state: State<PaymentScreenState> = _state
    init {
        initItems()
    }
    override fun initItems(){
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val client = PaymentService()
            _state.value = _state.value.copy(
                version = "1",
                loading = false,
                content = client.getContent()
            )
        }
    }

    override fun loadToday() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            var client = PaymentService()
            val values = client.getTodayUpdate()
            val newValues  = _state.value.content.toMutableList()
            newValues[0].updateContent(values)
            _state.value = _state.value.copy(
                loading= false,
                content = newValues
            )
        }
    }

    override fun newPayment(new: PaymentItem) {
        viewModelScope.launch {
            val values = _state.value.content.toMutableList()
            val newContent = arrayListOf<PaymentItem>(new)
            newContent.addAll(values[0].content)
            values[0].content = newContent
            _state.value = _state.value.copy(
                content = values
            )
        }
    }

}