# Spike M11 — LLM local on-device (achados e recomendação)

Data: 2026-11-02. Escopo: viabilidade de IA 100% offline para gerar
briefings/dicas narrativas dos casos, mantendo a premissa sem INTERNET.

## Opções avaliadas (fontes: docs.google.com/edge, dev.to, michalu.eu)

| Engine | Estado | Formato | Integração | Veredito |
|---|---|---|---|---|
| MediaPipe `tasks-genai` | **maintenance-only** (depreciada) | `.task` | simples, Kotlin | legada — não iniciar |
| **LiteRT-LM** `com.google.ai.edge.litertlm` | **sucessor oficial**, 0.18.0 (out/2026) | `.litertlm` | Engine+Conversation+Flow, coroutines nativo | **recomendada se avançar** |
| llama.cpp via JNI | vivo | `.gguf` | NDK manual, zoo de modelos | só se LiteRT não bastar |

## Modelos candidatos (HuggingFace / LiteRT Community)

| Modelo | Tamanho | Qualidade pt-BR | Observação |
|---|---|---|---|
| Gemma 3 270M IT q8 | **304 MB** | fraca–mediana | único que cabe leve no APK |
| Gemma 3 1B IT int4 | 584 MB | mediana | APK ~600MB, RAM ok em mid-range |
| Gemma 4 E2B int4 | 2,59 GB | boa | flagship-only (8–12GB RAM); fora de escopo |

## A restrição decisiva: offline × tamanho do modelo

O Google recomenda **download em runtime** ("model is too large to be
bundled in an APK") — incompatível com nossa premissa SEM permissão
INTERNET. Restam duas rotas:
1. Modelo embutido no APK/asset → só Gemma 3 270M (~300MB) é leve;
   qualidade de texto pt-BR é fraca para narrativa.
2. Download sob demanda → exigiria INTERNET = mudança de premissa (P7/ADR)
   + UX de baixar 300-600MB no primeiro uso.

## Custo real mesmo na rota viável

- `litertlm-android` adiciona dependência grande + ABI checks;
  **não roda em emulador** (só device real, GPU/high-end recomendado).
- Geração não-determinística quebra a reprodutibilidade do validador
  (OK para lore/briefing, NUNCA para dados validados pelo objectiveSql).
- Política de uso proibido da Google GenAI se aplica.

## Recomendação do spike

**M12 só se justifica como feature premium opcional** — LLM para sabor
narrativo (briefings dinâmicos) NÃO para mecânica. O ScenarioGenerator
determinístico (M10) já cobre a necessidade funcional de variação.
Sugestão: protótipo com Gemma 3 270M embutido em build variant "ai"
separado do release principal; se a qualidade pt-BR decepcionar,
descartar sem custo ao jogo. Decisão final fica com o usuário (M12 é
condicional ao resultado desta avaliação de custo-benefício).

## Implementação (M12 — 2026-11-09)

- Variant `aiDebug` (build type, `initWith debug`) — ADR-014. Tarefas do
  Gradle inalteradas; novo alvo `assembleAiDebug`.
- **Pin em `litertlm-android:0.16.1`** — a partir de 0.17.0 a biblioteca
  passa a Kotlin 2.4.0 e seu metadata é incompatível com o compilador
  Kotlin 2.2 embutido no AGP 9.4.1 (verificado 2026-11-09 no maven.google.com
  e em build local).
- Modelo `gemma3_270m.litertlm` vai em `app/src/aiDebug/assets/models/` —
  não versionado (`*.litertlm` no .gitignore); fonte no LEIA-ME.txt da pasta.
  No primeiro uso o app o copia para `filesDir/llm/` porque `EngineConfig`
  exige caminho de arquivo (assets não são paths).
- Ausência/falha do modelo degrada para `TemplateNarrativeGenerator` e a UI
  sinaliza "texto de reserva" — o jogo nunca quebra.
- Para testar em device real: `gradlew.bat installAiDebug` (LiteRT não roda
  em emulador).
