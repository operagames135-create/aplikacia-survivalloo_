package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.SurvivalLooApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SurvivalLooApp()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C0E14)
@Composable
fun SurvivalLooAppPreview() {
    MyApplicationTheme {
        SurvivalLooApp()
    }
}
