package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class GameSoundManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)
    var isSoundEnabled: Boolean = true
    var isHapticsEnabled: Boolean = true

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun playTone(freqHz: Float, durationMs: Int, volume: Float = 0.5f) {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                val sampleRate = 44100
                val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(100)
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i * freqHz / sampleRate
                    // Apply smooth envelope attack & release to avoid click
                    val attack = (i.toFloat() / (numSamples * 0.15f)).coerceAtMost(1f)
                    val decay = ((numSamples - i).toFloat() / (numSamples * 0.4f)).coerceIn(0f, 1f)
                    val sample = sin(angle) * attack * decay * Short.MAX_VALUE * volume
                    buffer[i] = sample.toInt().toShort()
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                // release track after playback
                scope.launch {
                    kotlinx.coroutines.delay(durationMs.toLong() + 50)
                    track.release()
                }
            } catch (_: Exception) {}
        }
    }

    fun playLetterSelect(indexInPath: Int) {
        // Pentatonic rising scale as letters connect: C5, D5, E5, G5, A5, C6
        val scale = listOf(523.25f, 587.33f, 659.25f, 783.99f, 880.00f, 1046.50f)
        val freq = scale.getOrElse(indexInPath) { 1046.50f }
        playTone(freq, durationMs = 60, volume = 0.4f)
        triggerHaptic(HapticType.LIGHT_TICK)
    }

    fun playWordSuccess() {
        // Joyful chime arpeggio: C5 -> E5 -> G5 -> C6
        scope.launch {
            playTone(523.25f, 90, 0.5f)
            kotlinx.coroutines.delay(80)
            playTone(659.25f, 90, 0.5f)
            kotlinx.coroutines.delay(80)
            playTone(783.99f, 90, 0.55f)
            kotlinx.coroutines.delay(80)
            playTone(1046.50f, 220, 0.6f)
        }
        triggerHaptic(HapticType.SUCCESS)
    }

    fun playBonusWord() {
        // Magical twin-sparkle tone
        scope.launch {
            playTone(880.00f, 80, 0.5f)
            kotlinx.coroutines.delay(70)
            playTone(1318.51f, 180, 0.6f)
        }
        triggerHaptic(HapticType.MEDIUM)
    }

    fun playInvalidWord() {
        // Gentle error low double buzz
        scope.launch {
            playTone(196.00f, 70, 0.4f)
            kotlinx.coroutines.delay(70)
            playTone(164.81f, 100, 0.4f)
        }
        triggerHaptic(HapticType.ERROR)
    }

    fun playLevelComplete() {
        // Victorious fanfare sequence
        scope.launch {
            val fanfare = listOf(523f, 659f, 783f, 1046f, 1318f)
            fanfare.forEach { f ->
                playTone(f, 120, 0.6f)
                kotlinx.coroutines.delay(100)
            }
        }
        triggerHaptic(HapticType.SUCCESS)
    }

    fun playShuffle() {
        playTone(600f, 50, 0.3f)
        triggerHaptic(HapticType.LIGHT_TICK)
    }

    fun playHint() {
        scope.launch {
            playTone(700f, 90, 0.5f)
            kotlinx.coroutines.delay(80)
            playTone(1100f, 150, 0.5f)
        }
        triggerHaptic(HapticType.LIGHT_TICK)
    }

    enum class HapticType {
        LIGHT_TICK,
        MEDIUM,
        SUCCESS,
        ERROR
    }

    fun triggerHaptic(type: HapticType) {
        if (!isHapticsEnabled) return
        try {
            val vib = vibrator ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val effect = when (type) {
                    HapticType.LIGHT_TICK -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    HapticType.MEDIUM -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    HapticType.SUCCESS -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                    HapticType.ERROR -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                }
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                val millis = when (type) {
                    HapticType.LIGHT_TICK -> 15L
                    HapticType.MEDIUM -> 30L
                    HapticType.SUCCESS -> 45L
                    HapticType.ERROR -> 60L
                }
                @Suppress("DEPRECATION")
                vib.vibrate(millis)
            }
        } catch (_: Exception) {}
    }
}
