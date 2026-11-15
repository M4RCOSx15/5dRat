# 5dRat

Jogo educativo de investigação SQL para Android — o jogador é um investigador
caçando "ratos" escondidos em bancos de dados, escrevendo consultas SQL reais
num sandbox isolado. 100% offline, sem permissão de INTERNET.

## Status

MVP completo — microtasks M0–M13 concluídas (ver `docs/PLAN.md`).

**Funciona no APK:**
- Navegação real entre 4 telas (Início, Missões, Laboratório, Perfil)
- Design system Material 3 com identidade própria (tema, chips semânticos,
  fontes embarcadas Plus Jakarta Sans + JetBrains Mono)
- **Campanha jogável**: 3 casos com briefing, desbloqueio progressivo e
  validação do ESTADO final via `MissionValidator` após cada execução
- **Laboratório com motor SQL real**: sandbox por caso (`{caseId}.db`)
  criado no armazenamento do app com seed determinística por runSeed —
  o jogador executa SELECT/UPDATE/DELETE/INSERT de verdade e vê o resultado
- **Gate de segurança**: uma instrução por vez, ATTACH/DETACH/PRAGMA/VACUUM
  bloqueados, literais e comentários anti-smuggling, modo read-only
- **Progresso persistente**: `SqliteProgressRepository` (SQLite puro,
  ADR-011) com eventos idempotentes; nível/XP/casos reais nas telas
- **Variant `aiDebug`** (opcional): narrativa por LiteRT-LM on-device com
  fallback determinístico — requer modelo `.litertlm` nos assets (fora do VCS)

**Pendências conhecidas:** keystore de release (artefato local, ADR-013) e
validação do `aiDebug` em dispositivo físico (LiteRT não roda em emulador).

## Build e teste

```bat
gradlew.bat assembleDebug        :: gera app\build\outputs\apk\debug\app-debug.apk
gradlew.bat testDebugUnitTest    :: testes JVM (domínio + gate SQL)
gradlew.bat ktlintCheck          :: lint de estilo
gradlew.bat connectedDebugAndroidTest  :: testes de instrumentação (requer device)
```

Instalar no celular: `adb install app\build\outputs\apk\debug\app-debug.apk`
ou copie o APK (fonte desconhecida habilitada).

## Arquitetura (resumo)

- **Dois bancos**: progresso (`progress.db`, SQLite puro — ADR-011) × sandbox
  de missão por caso (descartável, recriado de seed). O SQL do jogador nunca
  toca o banco de progresso.
- **Fronteiras**: `SqlExecutor`, `MissionValidator`, `ScenarioGenerator`,
  `ProgressRepository`, `NarrativeGenerator` — contratos Kotlin-puro.
- **Gate**: toda instrução do jogador passa pelo `SqlGate` antes de executar.
- Memória persistente do projeto: `docs/PLAN.md`, `docs/DECISIONS.md`,
  `docs/PROGRESS.md`, `docs/SECURITY.md`, `docs/PROTOTYPES.md`.
