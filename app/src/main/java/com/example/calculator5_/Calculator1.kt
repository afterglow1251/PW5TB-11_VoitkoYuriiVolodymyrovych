package com.example.calculator5_

import com.example.calculator5_.utils.validateInputs
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class ReliabilityIndicators(
    val omega: Double,
    val tV: Double,
    val mu: Double,
    val tP: Double
)

val dataIndicators = mapOf(
    "ПЛ-110 кВ" to ReliabilityIndicators(0.007, 10.0, 0.167, 35.0),
    "ПЛ-35 кВ" to ReliabilityIndicators(0.02, 8.0, 0.167, 35.0),
    "ПЛ-10 кВ" to ReliabilityIndicators(0.02, 10.0, 0.167, 35.0),
    "КЛ-10 кВ (траншея)" to ReliabilityIndicators(0.03, 44.0, 1.0, 9.0),
    "КЛ-10 кВ (кабельний канал)" to ReliabilityIndicators(0.005, 17.5, 1.0, 9.0),
    "T-110 кВ" to ReliabilityIndicators(0.015, 100.0, 1.0, 43.0),
    "T-35 кВ" to ReliabilityIndicators(0.02, 80.0, 1.0, 28.0),
    "T-10 кВ (кабельна мережа 10 кВ)" to ReliabilityIndicators(0.005, 60.0, 0.5, 10.0),
    "T-10 кВ (повітряна мережа 10 кВ)" to ReliabilityIndicators(0.05, 60.0, 0.5, 10.0),
    "B-110 кВ (елегазовий)" to ReliabilityIndicators(0.01, 30.0, 0.1, 30.0),
    "B-10 кВ (малооливний)" to ReliabilityIndicators(0.02, 15.0, 0.33, 15.0),
    "B-10 кВ (вакуумний)" to ReliabilityIndicators(0.01, 15.0, 0.33, 15.0),
    "Збірні шини 10 кВ на 1 приєднання" to ReliabilityIndicators(0.03, 2.0, 0.167, 5.0),
    "АВ-0,38 кВ" to ReliabilityIndicators(0.05, 4.0, 0.33, 10.0),
    "ЕД 6,10 кВ" to ReliabilityIndicators(0.1, 160.0, 0.5, 0.0),
    "ЕД 0,38 кВ" to ReliabilityIndicators(0.1, 50.0, 0.5, 0.0),
)

private fun createDefaultAmountMap(): Map<String, MutableState<String>> {
    val defaultValues = mapOf(
        "ПЛ-110 кВ" to "10",
        "ПЛ-35 кВ" to "0",
        "ПЛ-10 кВ" to "0",
        "КЛ-10 кВ (траншея)" to "0",
        "КЛ-10 кВ (кабельний канал)" to "0",
        "T-110 кВ" to "1",
        "T-35 кВ" to "0",
        "T-10 кВ (кабельна мережа 10 кВ)" to "0",
        "T-10 кВ (повітряна мережа 10 кВ)" to "0",
        "B-110 кВ (елегазовий)" to "1",
        "B-10 кВ (малооливний)" to "1",
        "B-10 кВ (вакуумний)" to "0",
        "Збірні шини 10 кВ на 1 приєднання" to "6",
        "АВ-0,38 кВ" to "0",
        "ЕД 6,10 кВ" to "0",
        "ЕД 0,38 кВ" to "0"
    )

    return defaultValues.mapValues { mutableStateOf(it.value) }
}


@Composable
fun Calculator1Screen(modifier: Modifier = Modifier) {

    val amountMap = remember { createDefaultAmountMap() }

    var resultText by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        amountMap.forEach { (key, state) ->
            OutlinedTextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = { Text(key) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Button(
            onClick = {
                val isValid = validateInputs(*amountMap.values.map { it.value }.toTypedArray())
                if (!isValid) {
                    showErrorDialog = true
                } else {
                    val results = calculateResults(amountMap, dataIndicators)

                    resultText = """
    Wос = ${results[0]} (рік^(-1))
    tв.ос = ${results[1]} (год)
    kа.ос = ${results[2]}
    kп.ос = ${results[3]}
    Wдк = ${results[4]} (рік^(-1))
    Wдс = ${results[5]} (рік^(-1))
""".trimIndent()

                    showResult = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Обчислити")
        }

        if (showResult) {
            Text(
                text = resultText,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    if (showErrorDialog) ErrorDialog(onDismiss = { showErrorDialog = false })
}


private fun calculateResults(
    amountMap: Map<String, MutableState<String>>,
    dataIndicators: Map<String, ReliabilityIndicators>
): List<Double> {
    var wOc = 0.0
    var tVOc = 0.0

    amountMap.forEach { (key, value) ->
        val amount = value.value.toIntOrNull() ?: 0
        val indicator = dataIndicators[key] ?: return@forEach

        if (amount > 0) {
            wOc += amount * indicator.omega
            tVOc += amount * indicator.tV * indicator.omega
        }
    }

    tVOc /= wOc
    val kAOc = (tVOc * wOc) / 8760
    val kPOs = 1.2 * 43 / 8760
    val wDk = 2 * wOc * (kAOc + kPOs)
    return listOf(wOc, tVOc, kAOc, kPOs, wDk, wDk + 0.02)
}

