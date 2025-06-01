# 💊 My Pills - Aplicativo Completo de Saúde e Vida Inteligente

![My Pills Logo](app/src/main/res/drawable/mypillsicon.webp)

**My Pills** é um aplicativo Android moderno e completo que integra gerenciamento de medicamentos, controle financeiro, informações de transporte público, lista de compras inteligente e assistente IA - tudo em uma única aplicação usando as mais modernas tecnologias do Android.

## 🌟 Funcionalidades Principais

### 💊 **Controle de Medicamentos**
- ✅ Scanner de código de barras para cadastro automático
- ⏰ Lembretes inteligentes com notificações
- 📊 Controle de estoque e validade
- 📈 Monitoramento de adesão ao tratamento
- 🏥 Histórico completo de uso

### 💰 **Gestão Financeira**
- 📊 Dashboard financeiro com métricas em tempo real
- 🧮 Calculadoras de juros compostos e empréstimos
- 💳 Controle de contas e transações
- 📊 Relatórios visuais e análises
- 💊 Rastreamento específico de gastos com medicamentos

### 🚌 **Informações de Transporte**
- 🗺️ Paradas de ônibus próximas com GPS
- ⏱️ Horários em tempo real
- 🛣️ Planejador de rotas inteligente
- ⭐ Rotas favoritas
- 📍 Integração com mapas

### 🛒 **Lista de Compras Inteligente**
- 💰 Controle orçamentário automático
- 🤖 IA para sugestões de economia
- 📊 Comparação de preços automática
- 📝 Listas organizadas por categoria
- 💡 Recomendações personalizadas

### 🤖 **Assistente IA Offline**
- 🔒 100% privado - funciona sem internet
- 💬 Chat natural sobre medicamentos e saúde
- 🎯 Sugestões contextuais inteligentes
- 🔄 Aprendizado local contínuo
- 📱 Integração com todas as funcionalidades

### ⏰ **Sistema de Lembretes**
- 🔔 Notificações inteligentes
- 🔁 Padrões de recorrência flexíveis
- 📊 Análise de adesão
- 🎵 Sons personalizáveis
- ⏰ Função soneca

### 📱 **Widgets Informativos**
- 💊 Widget de medicamentos do dia
- 💰 Resumo financeiro
- 🛒 Lista de compras ativa
- 🚌 Próximos ônibus

## 🏗️ Arquitetura Técnica

### **Stack Tecnológica**
- **🎨 UI**: Jetpack Compose + Material 3
- **🏛️ Arquitetura**: Clean Architecture + MVVM
- **🧭 Navegação**: Navigation Compose
- **🗄️ Banco**: Room Database
- **💉 DI**: Hilt (Dagger)
- **🔄 Reatividade**: Kotlin Flow + StateFlow
- **🤖 IA**: TensorFlow Lite (offline)
- **⚡ Background**: WorkManager
- **📱 Widgets**: Glance
- **📷 Câmera**: CameraX + ML Kit
- **📍 Localização**: Google Location Services

### **Padrões e Boas Práticas**
- ✅ Clean Architecture com separação clara de camadas
- ✅ Repository Pattern para abstração de dados
- ✅ Use Cases para lógica de negócio
- ✅ Dependency Injection com Hilt
- ✅ Reactive Programming com Flow
- ✅ Material Design 3 Guidelines
- ✅ Offline-first com Room Database
- ✅ Criptografia de dados sensíveis
- ✅ Testes unitários e de integração

## 📁 Estrutura do Projeto

```
app/
├── src/main/java/com/mypills/
│   ├── core/                           # Núcleo da aplicação
│   │   ├── database/                   # Room Database + DAOs
│   │   ├── di/                         # Dependency Injection
│   │   ├── navigation/                 # Navigation Compose
│   │   ├── theme/                      # Material 3 Theme
│   │   ├── utils/                      # Utilitários gerais
│   │   ├── settings/                   # Configurações do app
│   │   ├── security/                   # Biometria e segurança
│   │   └── analytics/                  # Analytics local
│   │
│   ├── features/                       # Funcionalidades por módulo
│   │   ├── medications/                # 💊 Medicamentos
│   │   │   ├── data/                   # Repository + API + Mappers
│   │   │   ├── domain/                 # Models + Use Cases
│   │   │   └── presentation/           # ViewModels + Composables
│   │   │
│   │   ├── finances/                   # 💰 Finanças
│   │   ├── transport/                  # 🚌 Transporte
│   │   ├── shopping/                   # 🛒 Compras
│   │   ├── assistant/                  # 🤖 IA Assistant
│   │   ├── reminders/                  # ⏰ Lembretes
│   │   ├── dashboard/                  # 📊 Dashboard
│   │   ├── settings/                   # ⚙️ Configurações
│   │   └── onboarding/                 # 👋 Onboarding
│   │
│   ├── widgets/                        # 📱 App Widgets (Glance)
│   └── MainActivity.kt                 # Activity principal
│
└── build.gradle.kts                    # Dependencies modernas
```

## 🚀 Como Executar o Projeto

### **Pré-requisitos**
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Android SDK 34
- Kotlin 1.9.22+

### **Configuração**

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/mypills.git
cd mypills
```

2. **Configurar APIs (Opcional)**
```kotlin
// local.properties
PRODUCT_API_BASE_URL="https://world.openfoodfacts.org/api/v0/"
TRANSPORT_API_BASE_URL="https://api.transport.local/"
PRICE_API_BASE_URL="https://api.prices.local/"
```

3. **Instalar dependências**
```bash
./gradlew build
```

4. **Executar no emulador/dispositivo**
```bash
./gradlew installDebug
```

### **Configuração de Desenvolvimento**

**Modelos de IA (Opcional)**
- Baixe os modelos TensorFlow Lite pré-treinados
- Coloque em `app/src/main/assets/`:
  - `intent_classifier.tflite`
  - `entity_extractor.tflite`
  - `vocabulary.txt`
  - `intent_labels.txt`
  - `entity_labels.txt`

## 📦 Build e Release

### **Debug Build**
```bash
./gradlew assembleDebug
```

### **Release Build**
```bash
./gradlew assembleRelease
```

### **Testes**
```bash
# Testes unitários
./gradlew testDebugUnitTest

# Testes instrumentados
./gradlew connectedDebugAndroidTest

# Todos os testes
./gradlew test
```

## 🔧 Configurações do Projeto

### **Permissões Necessárias**
- `CAMERA` - Scanner de código de barras
- `ACCESS_FINE_LOCATION` - Localização para transporte
- `POST_NOTIFICATIONS` - Lembretes
- `INTERNET` - APIs externas (opcional)
- `USE_BIOMETRIC` - Autenticação biométrica

### **Recursos Opcionais**
- Internet: Funciona 100% offline
- GPS: Melhora experiência de transporte
- Câmera: Para scanner de medicamentos
- Biometria: Segurança adicional

## 🎨 Temas e Personalização

### **Material 3 Theming**
- **🌓 Modo escuro/claro**: Automático ou manual
- **🎨 Cores dinâmicas**: Android 12+ (opcional)
- **🖌️ Tipografia**: Roboto personalizada
- **📐 Componentes**: Material 3 modernos

### **Cores por Módulo**
- 💊 **Medicamentos**: Verde (`#4CAF50`)
- 💰 **Finanças**: Laranja (`#FF9800`)
- 🚌 **Transporte**: Azul (`#2196F3`)
- 🛒 **Compras**: Roxo (`#9C27B0`)
- 🤖 **Assistente**: Azul Acinzentado (`#607D8B`)
- ⏰ **Lembretes**: Rosa (`#E91E63`)

## 🔒 Privacidade e Segurança

### **Privacidade by Design**
- ✅ **Dados 100% locais** - Nenhum dado enviado para servidores
- ✅ **IA offline** - TensorFlow Lite local
- ✅ **Criptografia** - Dados sensíveis criptografados
- ✅ **Biometria** - Autenticação segura opcional
- ✅ **Backup local** - Controle total dos dados

### **Conformidade**
- 🇧🇷 **LGPD** - Lei Geral de Proteção de Dados
- 🇪🇺 **GDPR** - Preparado para conformidade
- 🔒 **Sem rastreamento** - Zero analytics externos

## 📊 Métricas de Performance

### **Otimizações Implementadas**
- ⚡ **Lazy Loading** - Carregamento sob demanda
- 🗜️ **Proguard** - Ofuscação e otimização
- 📱 **App Bundle** - Distribuição otimizada
- 🧠 **Memory Management** - Gerenciamento eficiente
- 🔄 **Background Tasks** - WorkManager otimizado

### **Tamanho do App**
- 📦 **APK**: ~25-30MB
- 💾 **Instalado**: ~60-80MB
- 🤖 **Modelos IA**: ~10-15MB

## 🧪 Testes

### **Cobertura de Testes**
- ✅ **Unit Tests**: Use Cases e ViewModels
- ✅ **Integration Tests**: Repository e Database
- ✅ **UI Tests**: Compose e navegação
- ✅ **Performance Tests**: Memória e bateria

### **Executar Testes**
```bash
# Testes rápidos (unitários)
./gradlew test

# Testes completos (inclui UI)
./gradlew connectedAndroidTest

# Relatório de cobertura
./gradlew jacocoTestReport
```

## 🚀 Roadmap Futuro

### **v2.0 - Expansão**
- 🌐 **Sincronização** - Backup na nuvem opcional
- 👥 **Família** - Perfis múltiplos
- 📊 **Analytics** - Insights avançados
- 🔗 **Integração** - APIs de farmácias

### **v2.1 - Inteligência**
- 🧠 **IA Avançada** - Modelos maiores
- 📱 **Wear OS** - Smartwatch support
- 🎯 **ML Local** - Previsões personalizadas
- 🔊 **Comandos de Voz** - Assistente por voz

## 👥 Contribuição

### **Como Contribuir**
1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### **Guias de Contribuição**
- 📝 **Código**: Siga o Kotlin Style Guide
- 🧪 **Testes**: Adicione testes para novas funcionalidades
- 📖 **Documentação**: Atualize READMEs e comentários
- 🎨 **UI**: Mantenha consistência com Material 3

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Desenvolvimento

### **Arquitetura Detalhada**

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION                         │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐   │
│  │  Compose UI │  │  ViewModels  │  │  Navigation │   │
│  └─────────────┘  └──────────────┘  └─────────────┘   │
└─────────────────────────────────────────────────────────┘
                            │
┌─────────────────────────────────────────────────────────┐
│                     DOMAIN                              │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐   │
│  │   Models    │  │  Use Cases   │  │ Repositories│   │
│  └─────────────┘  └──────────────┘  └─────────────┘   │
└─────────────────────────────────────────────────────────┘
                            │
┌─────────────────────────────────────────────────────────┐
│                      DATA                               │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐   │
│  │ Room DB     │  │   APIs       │  │   Mappers   │   │
│  └─────────────┘  └──────────────┘  └─────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### **Fluxo de Dados**

```
User Input → Compose → ViewModel → Use Case → Repository → Data Source
                ↓
            StateFlow ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ←
                ↓
           Recomposition
```

## 🆘 Suporte

### **Documentação**
- 📚 **Wiki**: [GitHub Wiki](https://github.com/seu-usuario/mypills/wiki)
- 🎥 **Tutoriais**: [YouTube Playlist](https://youtube.com/playlist/...)
- 💬 **Comunidade**: [Discord Server](https://discord.gg/...)

### **Contato**
- 📧 **Email**: suporte@mypills.app
- 🐛 **Bugs**: [GitHub Issues](https://github.com/seu-usuario/mypills/issues)
- 💡 **Sugestões**: [Feature Requests](https://github.com/seu-usuario/mypills/discussions)

## 🙏 Agradecimentos

### **Tecnologias Utilizadas**
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material 3](https://m3.material.io/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt](https://dagger.dev/hilt/)
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [CameraX](https://developer.android.com/training/camerax)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)

### **Inspirações**
- Material Design Guidelines
- Android Architecture Components
- Clean Architecture Principles
- Modern Android Development

---

**Desenvolvido com ❤️ usando as mais modernas tecnologias Android**

*My Pills - Sua saúde e vida organizadas em um só lugar*