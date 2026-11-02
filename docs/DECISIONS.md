# DECISIONS.md — ADRs curtos

Formato: contexto → decisão → alternativas → consequências.

## ADR-001 — Fonte de verdade dos tokens de design
- **Contexto:** os HTMLs e o frontmatter do DESIGN.md usam a escala Material 3
  (`#f7faf4`, `#19432f`, `#d5e7db`…); o corpo do DESIGN.md descreve outra
  paleta (`#F8F9F6`, `#315B45`, `#E0EBF8`/`#EBE3F7`/`#F5E8D7`).
- **Decisão:** a escala Material 3 dos protótipos é a fonte de verdade; os
  acentos pastel do DESIGN.md viram cores semânticas de chips/tags
  (tabela=azul, coluna=lavanda, suspeito=bege, sucesso=menta).
- **Alternativas:** adotar o corpo do DESIGN.md inteiro (diverge das telas).
- **Consequências:** um único lugar para cores no tema Compose (M1b).

## ADR-002 — Injeção de dependências manual
- **Contexto:** o Prompt Mestre pede DI (Hilt, Koin ou manual) via ADR.
- **Decisão:** DI manual via factories de ViewModel no início (módulo único).
- **Alternativas:** Hilt (maduro, padrão de mercado, porém peso de setup para
  um app pequeno); Koin (runtime, menos verificável em compile time).
- **Consequências:** menos magia e build mais rápido; migrar para Hilt só com
  ganho concreto registrado em novo ADR.

## ADR-003 — Modelo de branches trunk-based
- **Contexto:** o repo tinha `main` e `develop` no mesmo commit.
- **Decisão:** trunk-based — microtasks em `m<N>/<slug>` com PR direto para
  `main`; `develop` removida para não criar bifurcação desnecessária.
- **Alternativas:** GitFlow (overkill para um desenvolvedor).
- **Consequências:** histórico linear, review por microtask.

## ADR-004 — Assets de imagem do produto
- **Contexto:** `inspirações/images (2).jfif` é stock Shutterstock com
  watermark — não pode ser embarcada. A captura em `inspirações` mostra outro
  pixel-art (silhueta com chamas em fundo vermelho).
- **Decisão:** o avatar robot_001 é o PNG da silhueta com chamas (confirmado
  pelo usuário em 2026-10-29). O jfif do rato NÃO entra no app — fica só como
  referência visual para a futura animação de captura (aranha com pegada
  cibernética pegando o rato, ideia do usuário).
- **Alternativas:** licenciar o stock (custo sem necessidade).
- **Consequências:** sem risco de licença; animação de captura fica registrada
  como ideia para M8+.

## ADR-005 — Versões da toolchain
- **Contexto:** versões precisam ser verificadas e fixas.
- **Decisão:** Gradle 9.6.0, AGP 9.4.1, Kotlin 2.4.21, JDK/toolchain 17,
  compileSdk/targetSdk 36, minSdk 26, ktlint-gradle 14.2.0, JUnit 4.13.2.
  Verificado em 2026-10-29 em developer.android.com e plugins.gradle.org.
- **Alternativas:** AGP 9.5.0 (lançado em 28/10/2026 — novo demais para adoção
  imediata); Kotlin 2.3.x (mais antigo, sem ganho).
- **Consequências:** previsibilidade; atualizações avaliadas por release notes.

## ADR-006 — minSdk 26
- **Contexto:** minSdk exige ADR.
- **Decisão:** minSdk 26 (Android 8.0) — cobre a grande maioria dos
  dispositivos e não bloqueia APIs necessárias ao projeto.
- **Alternativas:** 24 (alcance marginal, custo de compat); 28+ (exclui
  aparelhos antigos sem necessidade).
- **Consequências:** revisar quando surgir dependência de API >26.

## ADR-007 — Módulo único com pacotes por feature
- **Contexto:** o Prompt Mestre manda começar em módulo único.
- **Decisão:** `:app` único, pacotes por funcionalidade; camadas verificadas
  por teste de arquitetura quando o domínio existir.
- **Consequências:** modularização Gradle só com ganho concreto, via ADR.

## ADR-008 — allowBackup provisório
- **Contexto:** `allowBackup` e regras de extração exigem ADR.
- **Decisão:** `android:allowBackup="true"` provisório — progresso do jogador
  pode ser incluído em backup; bancos de missão são regeneráveis e serão
  excluídos via `dataExtractionRules` na M6/M13.
- **Consequências:** pendente de revisão na M13 (hardening).

## ADR-009 — Skeleton Android dentro da M1a
- **Contexto:** a M1a exige "CI verde" mas o repo era vazio.
- **Decisão:** criar um módulo `:app` mínimo (activity placeholder, sem
  Compose) só para a CI ter algo real para compilar/testar/lintar; a fundação
  visual e a navegação ficam na M1b como planejado.
- **Consequências:** CI já prova toolchain ponta a ponta; placeholder será
  substituído na M1b.

## ADR-010 — Versões Compose compatíveis com compileSdk 36
- **Contexto:** o Compose BOM `2026.09.00` (mais novo) resolve libs `1.12.x`
  que exigem `compileSdk >= 37`; `navigation-compose 2.10.x` idem. O
  compileSdk 36 está fixado em ADR-005 e mudá-lo exigiria novo ADR.
- **Decisão:** pin `compose-bom 2026.06.01` (ui `1.11.4`) e
  `navigation-compose 2.9.8`, ambos compilados contra SDK 36. Plugin
  `org.jetbrains.kotlin.plugin.compose` na versão `2.4.21`, casada com o
  Kotlin embutido no AGP 9.4.1 (exigido desde Kotlin 2.0 para `compose=true`).
- **Alternativas:** subir compileSdk para 37 (revisar ADR-005 quando o AGP
  exigir ou quando houver ganho real).
- **Consequências:** foundation compila e linta na SDK 36; upgrade do BOM
  avaliado junto com a decisão do compileSdk.

## ADR-011 — Progresso em SQLite puro (não Room) — 2026-11-01
Contexto: persistir progresso exige Room (anunciado na M6) + KSP casado com
Kotlin 2.4.x; a versão de KSP compatível não foi resolvida com segurança e
adicionar um processador de anotações só para 1 tabela é peso desmedido.
Decisão: `SqliteProgressRepository` usa android.database.sqlite direto —
progress.db separado dos sandboxes, eventos idempotentes por (mission_id,
kind), progresso derivado por agregação. Zero deps novas.
Consequências: revisitar Room se o esquema de progresso crescer (perfis,
conquistas complexas); o contrato ProgressRepository já isola a troca.
