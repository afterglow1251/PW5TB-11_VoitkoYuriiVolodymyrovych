package com.example.calculator5_

import com.example.calculator5_.utils.validateInputs
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun Calculator2Screen(modifier: Modifier = Modifier) {

    var omega by remember { mutableStateOf("0.01") }
    var tV by remember { mutableStateOf("0.045") }
    var pM by remember { mutableStateOf("5120") }
    var tM by remember { mutableStateOf("6451") }
    var kP by remember { mutableStateOf("0.004") }
    var zPerA by remember { mutableStateOf("23.6") }
    var zPerP by remember { mutableStateOf("17.6") }

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
        OutlinedTextField(
            value = omega,
            onValueChange = { omega = it },
            label = { Text("Omega") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tV,
            onValueChange = { tV = it },
            label = { Text("tS") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = pM,
            onValueChange = { pM = it },
            label = { Text("pM") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tM,
            onValueChange = { tM = it },
            label = { Text("tM") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = kP,
            onValueChange = { kP = it },
            label = { Text("kP") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = zPerA,
            onValueChange = { zPerA = it },
            label = { Text("zPerA") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = zPerP,
            onValueChange = { zPerP = it },
            label = { Text("zPerP") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val isValid = validateInputs(omega, tV, pM, tM, kP, zPerA, zPerP)
                if (!isValid) {
                    showErrorDialog = true
                } else {
                    val (mWnedA, mWnedP, mZper) = calculateResults(
                        omega.toDouble(),
                        tV.toDouble(),
                        pM.toDouble(),
                        tM.toDouble(),
                        kP.toDouble(),
                        zPerA.toDouble(),
                        zPerP.toDouble()
                    )

                    resultText = """
                        M(Wнед.а): %.2f (кВт * год)
                        M(Wед.п): %.2f (кВт * год)
                        M(Зпер): %.2f (грн)
                        """.trimIndent().format(mWnedA, mWnedP, mZper)

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
    omega: Double,
    tV: Double,
    pM: Double,
    tM: Double,
    kP: Double,
    zPerA: Double,
    zPerP: Double
): List<Double> {
    val mWnedA = omega * tV * pM * tM
    val mWnedP = kP * pM * tM
    val mZper = zPerA * mWnedA + zPerP * mWnedP

    return listOf(mWnedA, mWnedP, mZper)
}