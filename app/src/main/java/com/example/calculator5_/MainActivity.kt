package com.example.calculator5_

import com.example.calculator5_.ui.theme.Calculator5_Theme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.activity.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Calculator5_Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class Tab(val title: String) {
    CALCULATOR_1("Калькулятор 1"),
    CALCULATOR_2("Калькулятор 2")
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(Tab.CALCULATOR_1) }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.title) }
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        when (selectedTab) {
            Tab.CALCULATOR_1 -> Calculator1Screen(modifier = Modifier.padding(innerPadding))
            Tab.CALCULATOR_2 -> Calculator2Screen(modifier = Modifier.padding(innerPadding))
        }
    }
}
