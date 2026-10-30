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
