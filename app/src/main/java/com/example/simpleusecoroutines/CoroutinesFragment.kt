package com.example.simpleusecoroutines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.simpleusecoroutines.databinding.FragmentCoroutinesBinding
import kotlinx.coroutines.*

class CoroutinesFragment : Fragment() {
    private lateinit var binding: FragmentCoroutinesBinding

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main +
                exceptionHandler
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        evenAddCount = savedInstanceState?.getInt(KEY_EVEN_ADD) ?: 0
        oddAddCount = savedInstanceState?.getInt(KEY_ODD_ADD) ?: 0
        evenSubCount = savedInstanceState?.getInt(KEY_EVEN_SUB) ?: 0
        oddSubCount = savedInstanceState?.getInt(KEY_ODD_SUB) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoroutinesBinding.inflate(inflater, container, false)

        if(savedInstanceState != null){
            startCoroutines()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            startCoroutines()
        }

        binding.btnStop.setOnClickListener {
            stopCoroutines()
        }
    }

    private fun toggleButton(start: Boolean) {
        binding.btnStart.isEnabled = !start
        binding.btnStop.isEnabled = start
    }

    private suspend fun displayResult(text: String, view: TextView) =
        withContext(Dispatchers.Main) {
            view.text = text
        }

    private suspend fun runEvenAdd() {
        while (evenAddCount <= 100) {
            evenAddCount += 2
            displayResult("$evenAddCount", binding.firstCoroutineValue)
            delay(100)
        }
        binding.firstCoroutineValue.text = getString(R.string.finally_done)
    }

    private suspend fun runOddAdd() {
        while (oddAddCount <= 120) {
            oddAddCount += 3
            displayResult("$oddAddCount", binding.secondCoroutineValue)
            delay(200)
        }
        binding.secondCoroutineValue.text = getString(R.string.finally_done)
    }

    private suspend fun runEvenSub() {
        while (evenSubCount >= -140) {
            evenSubCount -= 2
            displayResult("$evenSubCount", binding.thirdCoroutineValue)
            delay(300)
        }
        binding.thirdCoroutineValue.text = getString(R.string.finally_done)
    }

    private suspend fun runOddSub() {
        while (oddSubCount >= -160) {
            oddSubCount -= 3
            displayResult("$oddSubCount", binding.fourthCoroutineValue)
            delay(400)
        }
        binding.fourthCoroutineValue.text = getString(R.string.finally_done)
    }

    private fun startCoroutines() {
        toggleButton(true)

        scope.launch {
            runEvenAdd()
        }

        scope.launch {
            runOddAdd()
        }

        scope.launch {
            runEvenSub()
        }

        scope.launch {
            runOddSub()
        }
    }

    private fun stopCoroutines() {
        toggleButton(false)
        zeroingCount()
        scope.cancel()

        scope = CoroutineScope(
            SupervisorJob() +
                    Dispatchers.Default
        )

        binding.firstCoroutineValue.text = getString(R.string.done)
        binding.secondCoroutineValue.text = getString(R.string.done)
        binding.thirdCoroutineValue.text = getString(R.string.done)
        binding.fourthCoroutineValue.text = getString(R.string.done)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_EVEN_ADD, evenAddCount)
        outState.putInt(KEY_ODD_ADD, oddAddCount)
        outState.putInt(KEY_EVEN_SUB, evenSubCount)
        outState.putInt(KEY_ODD_SUB, oddSubCount)
    }

    private fun zeroingCount() {
        evenAddCount = 0
        oddAddCount = 0
        evenSubCount = 0
        oddSubCount = 0
    }

    override fun onDestroyView() {
        scope.cancel()
        super.onDestroyView()
    }

    companion object {
        private var evenAddCount = 0
        private var oddAddCount = 0
        private var evenSubCount = 0
        private var oddSubCount = 0

        private const val KEY_EVEN_ADD = "keyEvenAdd"
        private const val KEY_ODD_ADD = "keyOddAdd"
        private const val KEY_EVEN_SUB = "keyEvenSub"
        private const val KEY_ODD_SUB = "keyOddSub"
    }
}