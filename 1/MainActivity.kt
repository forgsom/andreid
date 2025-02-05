package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab1.ui.theme.Lab1Theme

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SumOfMultiplesApp()
                }
            }
        }
    }
}

@Composable
fun SumOfMultiplesApp() {
    var inputNumbers by remember { mutableStateOf("") }
    var p by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = inputNumbers,
                onValueChange = { inputNumbers = it },
                label = { Text("Введите числа, разделенные запятыми") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = p,
                onValueChange = { p = it },
                label = { Text("Введите значение p") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val numbers = inputNumbers.split(",").mapNotNull { it.trim().toIntOrNull() }
                val pValue = p.toIntOrNull() ?: 1
                result = numbers.filter { it % pValue == 0 }.sum()
            }) {
                Text("Вычислить сумму")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Сумма элементов, кратных $p: $result")
        }
    }
}