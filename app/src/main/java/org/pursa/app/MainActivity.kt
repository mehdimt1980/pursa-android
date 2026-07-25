package org.pursa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.pursa.app.ui.PursaRoot
import org.pursa.app.ui.theme.PursaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PursaTheme {
                PursaRoot()
            }
        }
    }
}
