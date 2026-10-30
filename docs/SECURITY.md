# SECURITY.md — Modelo de ameaças do ambiente SQL

Estado: esqueleto inicial (M1a). Completado pela M1c (spike) e M7 (motor).

## Adversário
O próprio jogador (e, no futuro, a saída da LLM local).

## Ativos
- Banco de progresso (XP, conquistas, histórico) — não pode ser lido/alterado
  pela conexão do jogador.
- Integridade do validador de missões.
- Armazenamento e recursos do dispositivo (tempo de CPU, tamanho do banco).
- Privacidade: tudo local; sem conta, sem rede, alinhado à LGPD.

## Vetores previstos
Múltiplas instruções; ATTACH/DETACH; PRAGMAs perigosos; funções e extensões;
gatilhos, views e objetos temporários que contamine o validador; consultas de
custo explosivo (produto cartesiano, CTE recursiva); crescimento ilimitado do
banco; corrupção de esquema; vazamento de caminhos internos por mensagens de
erro.

## Defesa em profundidade (a verificar na M1c)
1. Separação física: conexões e arquivos distintos; missão nasce de cópia da
   seed; banco de progresso nunca aberto pela conexão do jogador.
2. Análise real do SQL pelo próprio SQLite (preparar cada instrução e provar
   que não sobra remanescente), nunca só regex.
3. Autorização por allowlist: callback de autorização do SQLite, conexão
   somente-leitura em modo consulta, limites de linguagem e tamanho,
   `trusted_schema` controlado.
4. Limites de recurso: timeout com cancelamento, máximo de linhas, tamanho
   máximo do banco e da instrução.
5. Validação em conexão confiável separada, somente-leitura, com consultas
   qualificadas e invariantes (contagem/checksum dos registros legítimos,
   fingerprint do esquema).
6. Recuperação por recriação da seed se a integridade for comprometida.
7. Erros mapeados para mensagens didáticas e saneadas.

## Pendências
- [ ] M1c: verificar o que o binding SQLite do Android expõe de fato
  (authorizer, limites, read-only) e registrar risco residual.
- [ ] M7: corpus de entradas maliciosas versionado; teste por controle.
- [ ] M13: checklist OWASP MASVS básico; revisão de dependências e segredos.

## Políticas já vigentes
- Sem permissão INTERNET (premissa; qualquer exceção exige aprovação).
- Componentes não exportados por padrão (apenas o LAUNCHER exportado).
- Nenhum log de SQL do jogador em release; sem telemetria sem aprovação.
- `allowBackup=true` provisório (ADR-008; revisão na M13).
- Chaves de assinatura de release fora do repositório.
