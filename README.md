# 5dRat — Find the Rat

Jogo Android de investigação e aprendizado de SQL inspirado em CTF
(Capture the Flag). O jogador explora bancos de dados para localizar e remover
o registro de um rato infiltrado — sem destruir dados legítimos. 100% offline,
sem conta, sem backend.

## Stack
Kotlin · Jetpack Compose (a partir da M1b) · SQLite/Room · Coroutines/Flow ·
arquitetura MVVM/UDF em módulo único. Veja `docs/ARCHITECTURE.md`.

## Como compilar e testar

Pré-requisitos: JDK 17+, Android SDK (platform `android-36`, build-tools
`36.0.0`). O Android Studio resolve os dois.

```bat
gradlew.bat assembleDebug      :: compila o APK de debug
gradlew.bat testDebugUnitTest  :: testes unitários (JVM)
gradlew.bat lintDebug          :: Android Lint
gradlew.bat ktlintCheck        :: formato/estilo (ktlint)
gradlew.bat ktlintFormat       :: corrige formatação automaticamente
```

No Windows sem PATH configurado, aponte o SDK em `local.properties`
(`sdk.dir=C:\\caminho\\para\\Sdk`) — arquivo local, não versionado.

## Fluxo de contribuição
Uma branch por microtask (`m<N>/<slug>`), PR para `main` com o template
preenchido, commits Conventional Commits. Regras completas em `AGENTS.md` e
backlog em `docs/PLAN.md`.

## Status
Fase de fundação — veja `docs/PLAN.md`.
