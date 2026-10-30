# PLAN.md — Backlog de microtasks

Ordem acordada (M0 entregue em 2026-10-29): M1a → M1b → M1c (spike,
descartável) → M2/M3/M4/M5 → M6 → M7 → M8 → M9 → M10 → M11 → M12 → M13.
Justificativa: a M1c é o maior risco técnico (isolamento SQL); deve terminar
antes que Laboratório (M4) e motor (M7) sejam construídos sobre premissas não
verificadas. M2–M5 são desacopladas do motor (executor fake restrito a
debug/previews). M6 pode rodar em paralelo às telas.

| ID | Título | Status | Branch | Dependências |
|----|--------|--------|--------|--------------|
| M0 | Auditoria e linha de base | ✅ Concluída | — | — |
| M1a | Governança e qualidade do repositório | ✅ Concluída | m1a/repo-governance | M0 |
| M1b | Fundação do projeto e design system | ✅ Concluída | m1b/design-foundation | M1a |
| M1c | Spike de viabilidade do isolamento SQL (descartável) | 🔄 Em andamento | spike/m1c-sql-isolation | M1a |
| M2 | Tela Início | ✅ Concluída | m2/home-screen | M1b |
| M3 | Tela Missões | ✅ Concluída | m3/missions-screen | M1b |
| M4 | Tela Laboratório | 🔄 Em andamento | m4/lab-screen | M1b, M1c |
| M5 | Tela Perfil | ✅ Concluída | m5/profile-screen | M1b |
| M6 | Persistência e domínio | ⬜ Pendente | m6/persistence-domain | M1a |
| M7 | Motor SQL isolado | ⬜ Pendente | m7/sql-sandbox | M1c, M6 |
| M8 | Primeira missão: "O rato no inventário" | ⬜ Pendente | m8/first-mission | M7, M3 |
| M9 | Progressão e recompensas | ⬜ Pendente | m9/progression | M6, M8 |
| M10 | Gerador procedural determinístico | ⬜ Pendente | m10/scenario-generator | M7 |
| M11 | Preparação para LLM local | ⬜ Pendente | m11/local-llm-spike | M10 |
| M12 | Integração controlada de IA (se M11 justificar) | ⬜ Pendente | m12/llm-integration | M11 |
| M13 | Hardening e entrega | ⬜ Pendente | m13/hardening | todas |

## Decisões pendentes resolvidas na M0
- Tokens de design: escala Material 3 dos protótipos + acentos do DESIGN.md
  como cores semânticas (ADR-001).
- Avatar robot_001: PNG pixel-art da silhueta com chamas (ADR-004).
- Animação de captura: aranha com pegada cibernética capturando o rato — o
  jfif do rato é apenas referência visual para essa animação (M8+).
