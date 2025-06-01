💊 My Pills - Aplicativo Completo de Saúde e Vida Inteligente

Status: 🚧 Em Desenvolvimento Ativo | Versão: 1.0.0-alpha | Target: Android API 24+

🎯 Visão Geral do Projeto
My Pills é um aplicativo Android nativo que integra 6 módulos principais em uma única solução de saúde e vida inteligente, utilizando Clean Architecture, Jetpack Compose e IA offline para máxima privacidade.
Conceito Principal
Centralizar o gerenciamento de medicamentos, controle financeiro, informações de transporte, lista de compras inteligente, sistema de lembretes e assistente IA em uma aplicação offline-first que prioriza privacidade e usabilidade.
🏗️ Arquitetura Técnica Detalhada
Stack Tecnológica Completa
kotlin// Apresentação
- Jetpack Compose (100% UI declarativa)
- Material Design 3 + Design System customizado
- Navigation Compose + Hilt Navigation
- StateFlow + Compose State

// Arquitetura
- Clean Architecture (Data/Domain/Presentation)
- MVVM + Repository Pattern
- Use Cases para lógica de negócio
- Dependency Injection com Hilt

// Dados & Persistência
- Room Database + Flow (reativo)
- DataStore Preferences (configurações)
- Kotlinx Serialization (JSON)
- Type Converters customizados

// Background & Notificações
- WorkManager (tarefas background)
- Notification Channels (lembretes)
- AlarmManager (precisão temporal)

// IA & ML
- TensorFlow Lite (IA offline)
- ML Kit (scanner código barras)
- CameraX (captura imagens)

// Integração Externa
- Retrofit + OkHttp (APIs opcionais)
- Glance (widgets home screen)
- Biometric API (autenticação)
Estrutura de Módulos
📁 app/src/main/java/com/mypills/
├── 🏛️ core/                          # Infraestrutura base
│   ├── database/                     # Room DB + DAOs + Entities
│   ├── di/                          # Hilt modules + providers
│   ├── navigation/                  # Nav graphs + destinations
│   ├── theme/                       # Material 3 + cores customizadas
│   ├── settings/                    # DataStore preferences
│   ├── security/                    # Biometria + criptografia
│   ├── analytics/                   # Analytics local (privado)
│   ├── backup/                      # Export/import dados
│   └── error/                       # Error handling centralizado
│
├── 🏥 features/                       # Módulos de funcionalidade
│   ├── 💊 medications/              # Controle de medicamentos
│   │   ├── data/                    # Repository + API + Mappers
│   │   │   ├── repository/          # Implementação repository
│   │   │   ├── api/                 # Retrofit services
│   │   │   └── mapper/              # Entity ↔ Domain mappers
│   │   ├── domain/                  # Business logic
│   │   │   ├── model/               # Domain models
│   │   │   ├── repository/          # Repository interfaces
│   │   │   └── usecase/             # Business use cases
│   │   └── presentation/            # UI Layer
│   │       ├── screen/              # Composable screens
│   │       ├── viewmodel/           # ViewModels + State
│   │       └── component/           # UI components
│   │
│   ├── 💰 finances/                 # Gestão financeira
│   ├── 🚌 transport/                # Informações transporte
│   ├── 🛒 shopping/                 # Lista compras inteligente
│   ├── ⏰ reminders/                # Sistema lembretes
│   ├── 🤖 assistant/                # IA conversacional
│   ├── 📊 dashboard/                # Tela principal
│   ├── ⚙️ settings/                 # Configurações
│   └── 👋 onboarding/              # Primeira execução
│
├── 📱 widgets/                       # App Widgets (Glance)
│   ├── medication/                  # Widget medicamentos
│   ├── finance/                     # Widget finanças
│   ├── shopping/                    # Widget lista compras
│   ├── transport/                   # Widget próximos ônibus
│   ├── data/                        # Data providers widgets
│   └── config/                      # Configuração widgets
│
└── MainActivity.kt                  # Single Activity + Navigation
📋 Status de Desenvolvimento por Módulo
✅ COMPLETO (Ready for Production)

✅ Core Architecture - Database, DI, Navigation, Theme
✅ Settings Module - Preferências, biometria, backup
✅ Onboarding - Primeira execução, permissões

🔶 EM DESENVOLVIMENTO (80-90% Complete)

🔶 Medications - Scanner, cadastro, controle estoque (falta: schedules)
🔶 Reminders - Criação, notificações (falta: recurring patterns)
🔶 Dashboard - Visão geral (falta: widgets dinâmicos)
🔶 Widgets - Layout base (falta: dados reais)

🚧 INICIADO (50-70% Complete)

🚧 Finances - Models, calculadoras (falta: relatórios)
🚧 Transport - Models, API base (falta: integração real)
🚧 Shopping - Lista básica (falta: IA otimização)
🚧 Assistant - TensorFlow base (falta: treinamento modelos)

❌ PENDENTE (Not Started)

❌ Tests - Unit, Integration, UI tests
❌ Performance - Otimizações, profiling
❌ Security - Criptografia, validações
❌ Analytics - Métricas locais

🎯 Funcionalidades Detalhadas
💊 Medications Module
kotlin// Features Implementadas:
✅ Scanner código de barras (CameraX + ML Kit)
✅ Cadastro manual medicamentos
✅ Controle de estoque atual
✅ Alertas de vencimento
✅ Histórico de uso

// Features Pendentes:
❌ Horários programados (MedicationSchedule)
❌ Interações medicamentosas
❌ Relatórios de adesão
❌ Sincronização com lembretes
💰 Finances Module
kotlin// Features Implementadas:
✅ Contas financeiras (checking, savings, etc)
✅ Transações (income, expense, transfer)
✅ Calculadoras (juros compostos, empréstimos)
✅ Orçamentos por categoria

// Features Pendentes:
❌ Relatórios visuais (gráficos)
❌ Categorização automática
❌ Metas financeiras
❌ Export para Excel/PDF
🚌 Transport Module
kotlin// Features Implementadas:
✅ Modelos de dados (routes, stops, arrivals)
✅ Busca de paradas próximas
✅ Planejador de rotas
✅ Rotas favoritas

// Features Pendentes:
❌ Integração API real de transporte
❌ Horários em tempo real
❌ Mapas interativos
❌ Notificações de atraso
🛒 Shopping Module
kotlin// Features Implementadas:
✅ Listas de compras
✅ Controle de orçamento
✅ Histórico de preços
✅ Sugestões de produtos

// Features Pendentes:
❌ IA para otimização de preços
❌ Comparação entre lojas
❌ Scanner produtos (barcode)
❌ Integração com APIs de preços
⏰ Reminders Module
kotlin// Features Implementadas:
✅ Lembretes básicos
✅ Notificações sistema
✅ Diferentes tipos (medicamento, consulta, etc)
✅ Prioridades

// Features Pendentes:
❌ Padrões de recorrência complexos
❌ Snooze inteligente
❌ Integração com calendário
❌ Lembretes por localização
🤖 Assistant Module
kotlin// Features Implementadas:
✅ Interface de chat
✅ TensorFlow Lite base
✅ Classificação de intenções
✅ Extração de entidades
✅ Respostas contextuais

// Features Pendentes:
❌ Treinamento modelos personalizados
❌ Integração com outros módulos
❌ Comandos de voz
❌ Sugestões proativas
🔧 Configuração de Desenvolvimento
Pré-requisitos Técnicos
bash# Ambiente obrigatório
Android Studio Hedgehog 2023.1.1+
JDK 17 (Oracle ou OpenJDK)
Android SDK 34
Kotlin 1.9.22+
Gradle 8.9

# Ferramentas recomendadas
Git 2.40+
ADB tools
Emulador Android API 34
Setup do Projeto
bash# 1. Clone e configuração inicial
git clone <repository-url>
cd mypills
./gradlew clean build

# 2. Configurar local.properties (opcional)
echo "PRODUCT_API_BASE_URL=https://world.openfoodfacts.org/api/v0/" >> local.properties
echo "TRANSPORT_API_BASE_URL=https://api.transport.local/" >> local.properties

# 3. Setup modelos IA (pendente)
# Baixar modelos TensorFlow Lite para:
# app/src/main/assets/intent_classifier.tflite
# app/src/main/assets/entity_extractor.tflite
Comandos de Build
bash# Debug build (desenvolvimento)
./gradlew assembleDebug

# Release build (produção)
./gradlew assembleRelease

# Testes (quando implementados)
./gradlew test
./gradlew connectedAndroidTest

# Linting e análise
./gradlew lint
./gradlew ktlintCheck
📱 Guia de Navegação e Telas
Fluxo Principal
kotlin// Splash → Onboarding (primeira vez) → Dashboard → Modules

Dashboard (home)
├── Quick Actions (6 cards)
├── Today's Medications (resumo)
├── Finance Summary (gastos mês)
├── Upcoming Reminders (próximos)
└── Transport Info (próximos ônibus)

Bottom Navigation:
├── 🏠 Dashboard
├── 💊 Medications (+ add, scanner)
├── ⏰ Reminders (tabs: today/upcoming/overdue)
├── 💰 Finances (tabs: overview/transactions/calculators)
├── 🚌 Transport (tabs: nearby/routes/favorites/planner)
├── 🛒 Shopping (lists + optimization)
└── 🤖 Assistant (chat interface)
Navegação Implementada
kotlin// Core routes funcionais:
"dashboard" -> DashboardScreen
"medications" -> MedicationsScreen
"add_medication" -> AddMedicationScreen
"reminders" -> RemindersScreen
"finances" -> FinancesScreen
"transport" -> TransportScreen
"shopping" -> ShoppingScreen
"assistant" -> AssistantScreen
"settings" -> SettingsScreen

// Deep linking pendente:
"medication_detail/{id}"
"shopping_list/{id}"
"chat/{conversationId}"
🗄️ Database Schema e Migrações
Entidades Principais
kotlin// Medications (3 tabelas)
MedicationEntity          // Dados básicos medicamento
MedicationScheduleEntity  // Horários programados
MedicationLogEntity       // Histórico de uso

// Finances (3 tabelas)
FinancialAccountEntity    // Contas bancárias
FinancialTransactionEntity // Transações
BudgetEntity             // Orçamentos

// Reminders (1 tabela)
ReminderEntity           // Lembretes sistema

// Transport (4 tabelas)
BusRouteEntity           // Rotas ônibus
BusStopEntity            // Paradas
RouteStopEntity          // Relação route-stop
FavoriteRouteEntity      // Rotas favoritas

// Shopping (3 tabelas)
ShoppingListEntity       // Listas compras
ShoppingItemEntity       // Itens das listas
PriceHistoryEntity       // Histórico preços

// Assistant (2 tabelas)
ConversationEntity       // Conversas chat
MessageEntity            // Mensagens

// Analytics (2 tabelas)
AppUsageEntity           // Uso do app
WidgetConfigEntity       // Configurações widgets
Type Converters Implementados
kotlinLocalDate ↔ String
LocalTime ↔ String
Instant ↔ Long
Set<DayOfWeek> ↔ String (JSON)
List<String> ↔ String (JSON)
Enums ↔ String
🎨 Design System e Temas
Cores por Módulo
kotlinval MedicationPrimary = Color(0xFF4CAF50)    // Verde
val FinancePrimary = Color(0xFFFF9800)       // Laranja
val TransportPrimary = Color(0xFF2196F3)     // Azul
val ShoppingPrimary = Color(0xFF9C27B0)      // Roxo
val AssistantPrimary = Color(0xFF607D8B)     // Azul acinzentado
val ReminderPrimary = Color(0xFFE91E63)      // Rosa
Componentes Customizados
kotlinModuleCard(module: String)           // Card com cor do módulo
StatusChip(text: String, status: String) // Chip de status
FinancialMetric()                    // Métricas financeiras
MedicationItem()                     // Item de medicamento
ReminderCard()                       // Card de lembrete
📋 Tarefas Pendentes Prioritárias
🚨 CRÍTICO (Semana 1-2)
kotlin// 1. Implementar testes básicos
├── MedicationsViewModelTest
├── FinancesRepositoryTest
├── ReminderUseCaseTest
└── DatabaseMigrationTest

// 2. Validações e error handling
├── Domain model validations
├── Repository error handling
├── ViewModel error states
└── UI error boundaries

// 3. Performance crítica
├── Database queries otimização
├── Compose recomposition debug
├── Memory leak detection
└── ANR prevention
⚠️ IMPORTANTE (Semana 3-4)
kotlin// 4. Security implementation
├── Database encryption (SQLCipher)
├── Biometric authentication
├── Data validation sanitization
└── ProGuard configuration

// 5. Features core completion
├── MedicationSchedule implementation
├── Recurring reminders pattern
├── Finance reports/charts
└── Assistant-modules integration
💡 ENHANCEMENT (Semana 5-6)
kotlin// 6. UI/UX improvements
├── Accessibility support
├── Dark theme refinement
├── Animation improvements
└── Loading states enhancement

// 7. Integration features
├── Transport API integration
├── Price comparison APIs
├── Export/Import functionality
└── Widget data providers
🧪 Estratégia de Testes
Testes a Implementar
kotlin// Unit Tests (core business logic)
src/test/kotlin/
├── domain/usecase/        // Use cases logic
├── data/repository/       // Repository implementations
├── presentation/viewmodel/ // ViewModel logic
└── core/utils/           // Utility functions

// Integration Tests (database + repositories)
src/androidTest/kotlin/
├── database/             // Room database tests
├── di/                   // Hilt integration tests
└── repository/           // Repository integration

// UI Tests (Compose)
src/androidTest/kotlin/ui/
├── medication/           // Medication screens
├── finance/              // Finance screens
├── navigation/           // Navigation tests
└── widget/               // Widget tests
Test Dependencies Necessárias
kotlin// Adicionar ao build.gradle.kts
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("app.cash.turbine:turbine:1.0.0") // Flow testing

androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
🔐 Segurança e Privacidade
Implementações Pendentes
kotlin// 1. Database encryption
implementation("net.zetetic:android-database-sqlcipher:4.5.4")

// 2. Biometric authentication enhancement
// 3. Data validation & sanitization
// 4. ProGuard rules completion
// 5. Certificate pinning (APIs)
Compliance Requirements

✅ LGPD/GDPR Ready - Dados 100% locais
✅ No Analytics Externos - Privacy by design
❌ Audit Logs - Pendente implementação
❌ Data Encryption - Pendente SQLCipher

🚀 Roadmap de Desenvolvimento
Versão 1.0.0 (MVP - 4-6 semanas)

 Testes unitários core (cobertura 70%+)
 Segurança básica (encryption + validation)
 Features core completas (medications + reminders)
 UI polish (accessibility + loading states)
 Performance optimization

Versão 1.1.0 (Enhanced - 2-3 semanas)

 Transport API integration
 Finance reports & charts
 Shopping price optimization
 Assistant advanced features
 Widget data providers

Versão 1.2.0 (Advanced - 3-4 semanas)

 Backup/restore cloud optional
 Multi-user profiles
 Wear OS companion
 Advanced analytics local
 Voice commands

📞 Informações de Desenvolvimento
Arquivos Críticos para Entender
kotlin// Entry points principais:
MainActivity.kt                    // Single activity
MyPillsNavigation.kt              // Navigation setup
AppDatabase.kt                    // Database central
DatabaseModule.kt                 // DI setup

// ViewModels principais:
MedicationsViewModel.kt           // Medications logic
RemindersViewModel.kt             // Reminders logic
DashboardViewModel.kt             // Dashboard state

// Repositories implementados:
MedicationRepositoryImpl.kt       // Medications data
RemindersRepositoryImpl.kt        // Reminders data
FinancialRepositoryImpl.kt        // Finance data
Padrões de Código Estabelecidos
kotlin// Naming conventions:
Features: PascalCase (MedicationsScreen)
Variables: camelCase (medicationList)
Constants: UPPER_SNAKE_CASE (MAX_RETRY_COUNT)
Files: PascalCase + suffix (MedicationsViewModel)

// State management:
StateFlow para UI state
Flow para data streams
Result<T> para operations (pendente)

// Error handling:
try-catch em ViewModels
Result wrapper em Use Cases (pendente)
Error states em UI

Este README serve como documentação técnica completa para desenvolvimento contínuo do projeto My Pills. Mantenha atualizado conforme progresso.
Última atualização: Janeiro 2025