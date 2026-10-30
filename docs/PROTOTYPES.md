# PROTOTYPES.md — Tokens extraídos e inconsistências dos protótipos

Fonte: `inspirações/stitch_5drat_sql_investigation_app.zip` (4 telas Stitch +
`detective_query/DESIGN.md`), avatar e captura em `inspirações/`.
Analisado em 2026-10-29 (M0).

## Tokens (fonte de verdade: escala Material 3 dos tailwind-configs — ADR-001)

| Token | Valor | Uso |
|-------|-------|-----|
| background / surface / surface-bright | `#F7FAF4` | fundo off-white |
| surface-container-lowest | `#FFFFFF` | cartões |
| surface-container-low | `#F2F5EE` | áreas de apoio |
| surface-container(-high/-highest) | `#ECEFE8` / `#E6E9E3` / `#E0E3DD` | camadas |
| primary | `#19432F` | ações, títulos fortes |
| primary-container | `#315B45` | destaque verde |
| secondary | `#526259` | textos/ícones secundários |
| secondary-container | `#D5E7DB` | contêineres e destaques suaves (menta) |
| tertiary-fixed | `#D9E4F1` | azul claro informacional |
| on-surface / on-background | `#191D19` | texto principal |
| on-surface-variant | `#414943` | texto secundário |
| outline / outline-variant | `#717973` / `#C1C8C1` | bordas |
| error / error-container | `#BA1A1A` / `#FFDAD6` | erros |
| primary-fixed(-dim) | `#BFEDD0` / `#A3D1B5` | pill ativa da nav |

### Acentos semânticos (corpo do DESIGN.md, usados em chips/tags — ADR-001)
Tabela/entidade: `#E0EBF8`/`#1E3A8A` · Coluna/variável: `#EBE3F7`/`#4C1D95` ·
Suspeito: `#F5E8D7`/`#78350F` · Sucesso: `#DDEFE3`/`#1B4332`.

### Tipografia
Plus Jakarta Sans (UI) e JetBrains Mono (SQL/código/etiquetas técnicas).
Embarcar as fontes localmente (verificar licença — ambas OFL); sem download
em runtime. Escala: headline-xl 36/44 → headline-sm 18/24; body 12–16;
code 11–15; label 11–12.

### Forma/espaço
Raio: 4/8/12/16/24px e pill; espaçamento base 4/8px, margem mobile 16px;
elevação por camadas sutis (borda 1px + sombras ambientes leves).

## Inconsistências registradas (não reproduzir os defeitos)
| # | Inconsistência |
|---|----------------|
| P1 | Avatar do Perfil aponta para URL externa (googleusercontent) + SVG placeholder; deve usar a imagem local (ADR-004), sem suavização (FilterQuality.None). |
| P2 | Duas paletas nos materiais — resolvido por ADR-001. |
| P3 | Header da tela Missões exibe subtítulo "Início" (defeito de cópia). |
| P4 | Nav do Perfil usa ícone `travel_explore` para Missões; demais telas usam `explore`. Padronizar `explore`. |
| P5 | Estado ativo da nav não renderiza em Início e Missões (depende de script do shell de preview). Implementar estado ativo real. |
| P6 | Dados ilustrativos conflitantes: "3 ratos capturados" e "O rato no inventário Concluída" vs. CASE_001 "EM ANDAMENTO" e "01 concluída"; `CASE_FILE #042` vs. CASE_001–003. Tratar como demo, nunca como dado real. |
| P7 | Dependências externas de runtime (Tailwind/Google Fonts/Material Symbols CDNs, logo via lh3.googleusercontent). Tudo deve virar recurso local. |
| P8 | Claims decorativos ("ENCRYPTED LOCAL STORAGE · READY", "BUILD v0.4.2-alpha", busca e settings sem comportamento). Não reproduzir como reais. |

## Notas de produto
- Animação de captura (ideia do usuário, 2026-10-29): ao encontrar o rato,
  uma aranha com pegada cibernética o captura — animação simples. O jfif do
  rato serve apenas como referência visual para essa arte (não versionar).
