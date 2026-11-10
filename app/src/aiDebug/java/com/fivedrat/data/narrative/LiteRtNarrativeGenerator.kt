package com.fivedrat.data.narrative

import android.content.Context
import android.util.Log
import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.narrative.NarrativeGenerator
import com.fivedrat.domain.narrative.NarrativePrompt
import com.fivedrat.domain.narrative.NarrativeResult
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Narrativa por LLM on-device via LiteRT-LM (M12, somente variant aiDebug).
 *
 * O modelo (.litertlm) fica nos assets do variant e nunca é versionado
 * (~300 MB). Na primeira chamada ele é materializado em filesDir, porque o
 * EngineConfig exige um caminho de arquivo. Qualquer ausência ou falha do
 * modelo degrada para a narrativa de reserva — nunca quebra o jogo.
 */
class LiteRtNarrativeGenerator(
    private val context: Context,
) : NarrativeGenerator {
    private val modelFile = File(context.filesDir, "llm/$MODEL_FILE_NAME")

    override suspend fun briefing(case: Case): NarrativeResult =
        withContext(Dispatchers.IO) {
            if (!ensureModel()) {
                Log.w(TAG, "Modelo ausente nos assets — usando narrativa de reserva.")
                return@withContext fallback(case)
            }
            try {
                generate(case)
            } catch (e: Exception) {
                Log.w(TAG, "LiteRT falhou (${e.message}) — narrativa de reserva.", e)
                fallback(case)
            }
        }

    private fun ensureModel(): Boolean {
        if (modelFile.isFile) return true
        return runCatching {
            context.assets.open("models/$MODEL_FILE_NAME").use { input ->
                modelFile.parentFile?.mkdirs()
                modelFile.outputStream().use(input::copyTo)
            }
        }.isSuccess
    }

    private suspend fun generate(case: Case): NarrativeResult {
        val text =
            Engine(
                EngineConfig(
                    modelPath = modelFile.path,
                    backend = Backend.GPU(),
                    cacheDir = context.cacheDir.path,
                ),
            ).use { engine ->
                engine.initialize()
                engine
                    .createConversation(
                        ConversationConfig(
                            systemInstruction =
                                Contents.of(NarrativePrompt.SYSTEM_INSTRUCTION),
                        ),
                    ).use { conversation ->
                        conversation
                            .sendMessageAsync(Message.user(NarrativePrompt.build(case)))
                            .fold(StringBuilder()) { acc, chunk -> acc.append(chunk) }
                            .toString()
                    }
            }
        return text.trim().takeIf { it.isNotEmpty() }?.let {
            NarrativeResult(text = it, aiGenerated = true)
        } ?: fallback(case)
    }

    private suspend fun fallback(case: Case): NarrativeResult = TemplateNarrativeGenerator().briefing(case)

    private companion object {
        const val TAG = "LiteRtNarrative"
        const val MODEL_FILE_NAME = "gemma3_270m.litertlm"
    }
}
