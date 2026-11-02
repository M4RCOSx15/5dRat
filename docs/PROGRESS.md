# PROGRESS.md — Diário de sessão

## 2026-10-29 — M0: Auditoria e linha de base (concluída)
- Repositório greenfield: 1 commit, só README. Remote: github.com/M4RCOSx15/5dRat.
- Ambiente: JDK 24 + JRE 8, Git 2.49, Node; Android Studio instalado
  (C:\Program Files\Android\Android Studio, build AI-262.x, JBR 25);
  **Android SDK ainda não baixado** (será feito na 1ª execução do Studio).
- Protótipos analisados: 4 telas + DESIGN.md + assets; inconsistências em
  docs/PROTOTYPES.md. Achado: jfif é stock com watermark → não usável.
- Relatório M0 entregue e aprovado pelo usuário com 6 decisões respondidas.
- Limitação: sem SDK local, compilação/testes não executados — CI no GitHub
  Actions valida a toolchain ponta a ponta.
- Próximo passo: M1a.

## 2026-10-29 — M1a: Governança e qualidade do repositório (em andamento)
- Plano: AGENTS.md, docs/ (PLAN, DECISIONS, PROGRESS, ARCHITECTURE, SECURITY,
  PROTOTYPES), README, .gitignore, .editorconfig, .gitattributes, template de
  PR, catálogo de versões, skeleton Gradle mínimo (módulo :app placeholder) e
  CI mínima (ktlint, assembleDebug, testDebugUnitTest, lintDebug) com actions
  fixadas por hash. ADR-009 registra por que o skeleton entra na M1a.
- Verificações locais: sem Android SDK na máquina → build/testes delegados à
  CI; validação local limitada ao wrapper (`gradlew help`).

## 2026-10-29 — M1a: fechamento (continuação)
- Revisão do estado: todos os artefatos da governança já estavam criados na
  branch `m1a/repo-governance`, ainda não commitados.
- Correção pré-commit: relatório local do ktlint apontava 3 violações
  `multiline-expression-wrapping` em `app/build.gradle.kts`
  (`libs.versions.*.get().toInt()` quebrado em linhas) — corrigidas para
  linha única; sem isso a etapa `ktlintCheck` da CI falharia.
- Commits individuais por arquivo (Conventional Commits) e push da branch
  para `origin`; PR para `main` aguardando aprovação do usuário.
- Limitação mantida: sem Android SDK local, `assembleDebug`/`testDebugUnitTest`
  serão provados somente na CI do GitHub Actions.

## 2026-10-29 — M1a aprovada (PR #1 merge) + M1b (em andamento)
- PR #1 mergeado na main. Android Studio abriu e baixou o SDK; com
  `local.properties` (sdk.dir) o AGP auto-instalou a plataforma `android-36` —
  builds locais passaram a funcionar (`assembleDebug` OK).
- `local.properties` corrigido para forward slashes (escape `\U` quebrava path).
- M1b: catálogo Compose (BOM 2026.09.00, activity-compose 1.13.0,
  navigation-compose 2.10.2 — verificado em dl.google.com), tema M3 com os
  tokens do ADR-001, cores semânticas de chips, tipografia, NavigationBar com
  4 destinos e placeholders (telas reais nas M2–M5).
- Fontes embarcadas adiadas: `res/font` precisa dos TTFs (Plus Jakarta Sans,
  JetBrains Mono — OFL); Type.kt usa fontes de sistema com TODO registrado.

## 2026-10-30 — M1b aprovada (PR #2 merge) + M1c spike (em revisão)
- CI: fix `chmod +x gradlew` aplicado e mergeado (PR #2).
- M1c: spike de isolamento SQL com sqlite-jdbc 3.54.1.0 em teste JVM.
  Achados registrados em SECURITY.md: ATTACH escapa para arquivos irmãos
  (gate na M7 obrigatório — binding não expõe authorizer), read-only
  bloqueia escrita, JDBC roda só a 1ª instrução de input empilhado.
- androidTest `SqlSandboxAndroidSpikeTest` commitado para validação em
  dispositivo; execução pendente (sem emulador/AVD local).
- Branch `spike/m1c-sql-isolation` é descartável por definição — decisão de
  merge fica com o usuário.

## 2026-10-30 — M2: Tela Início (em revisão)
- HomeScreen implementada do protótipo 5drat_in_cio: cartão "MISSÃO ATUAL"
  (primary-container + hint SQL em CodeTextStyle), atalhos, Dica do Detetive.
- Dados ilustrativos (XP, contagens, CASE_FILE #) NÃO reproduzidos (P6/P8) —
  fonte real chega na M6. Botões navegam para missions/lab via NavController.
- Verificações locais: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M2 aprovada (PR #4 merge) + M3: Tela Missões (em revisão)
- MissionsScreen do protótipo 5drat_miss_es: título "Escolha seu próximo caso",
  contagem derivada da lista real de casos (sem números ilustrativos — P6/P8),
  cards CASE_001/002 disponíveis (PlayArrow) e CASE_003 bloqueado (Lock).
- Lista de casos estática provisória; bloqueio dinâmico por progresso vem da M6.
- Verificações locais: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M3 aprovada (PR #5 merge) + M5: Tela Perfil (em revisão)
- ProfileScreen do protótipo 5drat_perfil: avatar robot_001 local (drawable,
  FilterQuality.None — P1/ADR-004), divisão, seções Evolução/Histórico com
  placeholders honestos (sem métricas ilustrativas — P6/P8; dados na M6).
- Verificações locais: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M5 aprovada (PR #6 merge) + M4: Tela Laboratório (em revisão)
- LabScreen do protótipo 5drat_laborat_rio: schema do banco com chips de
  tabela usando SemanticChipColors (design system), console SQL com editor
  mono (CodeTextStyle), Executar/Limpar e área de resultado em estado vazio
  honesto — execução real é da M7 (motor isolado, achados da M1c).
- Verificações locais: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M4 aprovada (PR #7 merge) + M6: Domínio e ProgressRepository (em revisão)
- domain/ Kotlin-puro (ADR-007): MissionState, Mission, PlayerProgress.
- Contrato ProgressRepository (fronteira seção 5): Flow de progresso,
  recordRatCaptured/recordMissionCompleted idempotentes por missionId.
- data/InMemoryProgressRepository: impl provisória com estado real para as
  telas; persistência Room/DataStore permanece na M6 (KSP/versões a fixar).
- Testes JVM do domínio: 3/3 verdes (idempotência, contagem, nível por XP).
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M6 aprovada (PR #8 merge) + M7: motor SQL (em revisão)
- domain/sql/SqlExecutor: contrato Kotlin-puro com SqlResult selado
  (Success/Blocked/Error).
- data/sql/SqlGate: portão de instrução única — ATTACH/DETACH/PRAGMA/VACUUM
  bloqueados (vetor provado no spike M1c), modo readOnly restringe a SELECT,
  literais/comentários neutralizados, multi-statement rejeitado, limite de
  8KB. Kotlin puro, 9 testes JVM verdes.
- data/sql/AndroidSqlExecutor: executor real sobre arquivo scratch via
  SQLiteDatabase (OPEN_READONLY em modo consulta), maxRows=500, erros
  mapeados — nunca toca o banco de progresso.
- Pendente nesta M7: ScenarioGenerator/seeds, MissionValidator, fiação do
  LabScreen ao executor e validação de integridade do scratch.

## 2026-10-30 — M7 fiação: LabScreen executa SQL real + README
- LabScreen conectado ao AndroidSqlExecutor: botão Executar roda no sandbox
  DemoScratchDb (loja_demo.db em filesDir, seed produtos/clientes/pedidos) em
  Dispatchers.IO; resultado mostra colunas/linhas, Blocked/Error mapeados.
- README.md criado: status do MVP, build/teste, resumo da arquitetura.
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — Popup de captura: aranha rato pixel art (M7 fiação)
- RatCapturedDialog: Dialog centrado (~85% largura) com VideoView built-in
  reproduzindo aranha_rato_pixelart.mp4 (res/raw, loop) — sem dependência
  de player pesado (P4). Fecha em Confirmar/fora.
- LabScreen detecta captura via mini-validador de estado: após cada execução,
  SELECT count(*) confere se o rato sumiu do sandbox (estado, não SQL) → popup.
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — Fixes de UX reportados em teste no device
- Missões: ícone play virou IconButton → navega ao Laboratório.
- LabScreen: checagem inicial do rato na abertura (LaunchedEffect) — popup
  só dispara na transição presente→ausente, não em qualquer query com rato
  já morto (bug do SELECT default reportado).
- RatCapturedDialog: botão "Próxima missão" em tertiaryContainer (verde
  suave do tema) abaixo do vídeo → navega a Missões.
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-10-30 — M8: loop jogável completo (em revisão)
- domain/mission: Campaign com 3 casos reais (schema+seed+objectiveSql+XP);
  MissionValidator confere estado final via executor readonly.
- data/sql/CaseSandbox: sandbox {caseId}.db por caso + reset. AppServices:
  DI manual (repo progresso + caso ativo).
- LabScreen: briefing do caso, chips de tabelas, executa no sandbox do caso,
  valida objetivo → popup de captura → registra missão+rato no repositório.
- MissionsScreen: campanha real, desbloqueio por completadas (índice<=n),
  CheckCircle em concluídas, play seleciona caso → lab.
- Merge de fixes m7 não-mergeados (PR #10 foi antes do push b9ea1b8):
  conflitos resolvidos mantendo reescritas M8.
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-11-01 — M8 aprovada (PR #11) + persistência real de progresso
- SqliteProgressRepository: progress.db com mission_events idempotente
  (INSERT OR IGNORE por mission_id+kind), agregação para xp/level/completed/
  rats — progresso agora SOBREVIVE a reinício do app (ADR-011).
- AppServices.progress(context) lazy; telas usam o repo real.
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.

## 2026-11-01 — m6b aprovada (PR #12) + M9: UI viva com progresso real
- ProfileScreen: nível/XP/casos/ratos reais via ProgressRepository + Campaign.
- HomeScreen: missão atual = primeiro caso incompleto, XP/nível reais,
  botão adapta (Continuar missão → lab do caso | Rever casos → missões).
- Verificações: ktlintCheck, assembleDebug, testDebugUnitTest — verde.
