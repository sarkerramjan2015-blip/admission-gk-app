package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.GKRepository
import com.example.data.SeedData
import com.example.ui.GKViewModel
import com.example.ui.MainApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val db = AppDatabase.getDatabase(this)
        val repository = GKRepository(db.gkDao())
        
        SeedData.populateDatabase(this, repository)
        
        setContent {
            MyApplicationTheme {
                val viewModel: GKViewModel = viewModel(factory = GKViewModel.Factory(repository))
                MainApp(viewModel)
            }
        }
    }
}
