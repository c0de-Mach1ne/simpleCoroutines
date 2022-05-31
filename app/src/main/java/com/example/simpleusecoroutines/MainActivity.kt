package com.example.simpleusecoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simpleusecoroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val coroutinesFragment: CoroutinesFragment
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as CoroutinesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragment_container_view, CoroutinesFragment())
                commit()
            }
        }
    }
}