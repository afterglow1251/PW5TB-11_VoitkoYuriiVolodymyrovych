package com.example.calculator5_.utils

fun validateInputs(vararg values: String): Boolean {
    return values.all { it.toDoubleOrNull() != null }
}