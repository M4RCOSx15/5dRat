# 5dRat

Jogo educativo de investigação SQL para Android — o jogador é um investigador
caçando "ratos" escondidos em bancos de dados, escrevendo consultas SQL reais
num sandbox isolado. 100% offline, sem permissão de INTERNET.

## Status

MVP em construção (microtasks M0–M13, ver `docs/PLAN.md`).

**Já funciona no APK:**
- Navegação real entre 4 telas (Início, Missões, Laboratório, Perfil)
- Design system Material 3 com identidade própria (tema, chips semânticos,
  fonte mono para SQL)
- **Laboratório com motor SQL real**: sandbox `loja_demo.db` (produtos,
  clientes, pedidos) criado no armazenamento do app — o jogador executa
  SELECT/UPDATE/DELETE/INSERT de verdade e vê o resultado
- **Gate de segurança** (spike M1c): uma instrução por vez, ATTACH/DETACH/
  PRAGMA/VACUUM bloqueados, literais e comentários anti-smuggling
- Domínio Kotlin-puro + `ProgressRepository` (idempotente) com impl em memória

**Ainda pendente:** persistência real (Room/DataStore), validador de missões,
gerador de cenários, progressão real nas telas.

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

- **Dois bancos**: progresso (Room, sagrado) × sandbox de missão (descartável,
  recriado de seed). O SQL do jogador nunca toca o banco de progresso.
- **Fronteiras da seção 5**: `SqlExecutor`, `MissionValidator`,
  `ScenarioGenerator`, `ProgressRepository` — contratos Kotlin-puro.
- **Gate**: toda instrução do jogador passa pelo `SqlGate` antes de executar.
- Memória persistente do projeto: `docs/PLAN.md`, `docs/DECISIONS.md`,
  `docs/PROGRESS.md`, `docs/SECURITY.md`, `docs/PROTOTYPES.md`.
