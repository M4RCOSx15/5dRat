# Revisão OWASP MASVS — M13 hardening (2026-11-06)

Revisão básica por categoria MASVS do app 5dRat (offline, sem INTERNET).

## Storage (MASVS-STORAGE)

- ✅ Dados em diretório privado do app (`filesDir`): `progress.db` e
  sandboxes `{case}.db` — nada em storage externo/SharedPreferences em
  claro (não há SharedPreferences).
- ✅ Sem segredos/chaves no app — nenhum token, API key ou credencial
  existe no código (offline por premissa).
- ⚠️ `allowBackup=true` mantido (ADR-008): dados de progresso de jogo são
  não-sensíveis; backup habilitado preserva XP/missões em troca de
  aparelho. Reavaliar se dados sensíveis surgirem.

## Network (MASVS-NETWORK)

- ✅ N/A por design: `INTERNET` não declarada — nenhum tráfego possível;
  cleartext irrelevante; sem certificados/TLS para configurar.

## Resilience / anti-tamper (MASVS-RESILIENCE)

- ✅ **R8 minify + shrinkResources ativados** em release (M13): reduz
  superfície de engenharia reversa sobre seeds e regras do jogo.
- ✅ SQL do jogador passa pelo `SqlGate` (ATTACH/DETACH/PRAGMA bloqueados,
  instrução única) — provado no spike M1c e em testes JVM.
- ✅ Validador verifica estado final em conexão separada somente-leitura.
- Sandboxes descartáveis por caso; progress.db nunca aberto pelo executor.

## Platform interaction (MASVS-PLATFORM)

- ✅ Somente `MainActivity` exportada (LAUNCHER) — sem providers, services
  ou receivers expostos; sem deep links/WebView/IPC.
- ✅ Sem permissões declaradas além do implícito — zero dados do usuário
  coletados.

## Code quality / privacy (MASVS-CODE)

- ✅ Nenhum `Log.*` em código de produção (grep zero hits).
- ✅ Sem telemetria, analytics ou ads.
- ✅ Deps mínimas e pinadas: compose BOM, material3, icons, navigation,
  junit (test), sqlite-jdbc (test only), androidx.test (androidTest).
- ✅ Testes: gate SQL (9), gerador (4), progresso (3), sanity.
- ⚠️ Pendente: keystore de release (signing) — fora do VCS por política;
  gerar para distribuição Play/sideload assinado.

## Conclusão

Postura adequada ao MVP offline: superfície de ataque mínima por design
(sem rede, sem IPC, sem permissões), controles de sandbox SQL verificados,
ofuscação de release ativa. Único pendente real: assinatura de release.
