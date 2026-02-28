# 🔋 BatteryPulseAI

<p align="center">
  <img src="https://img.shields.io/badge/Android%2015-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android 15" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/TensorFlow%20Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white" alt="TensorFlow Lite" />
  <img src="https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3" />
</p>

BatteryPulseAI is an intelligent, autonomous agent-based Android application designed to predict battery lifetime and optimize smartphone energy consumption. Built with native Android 15 (Kotlin & Jetpack Compose), it leverages concepts from 2011 prediction papers and modernizes them with LSTM-Transformer models, 5G RRC optimization, comprehensive device state tracking, and On-device AI diagnostics.

## 🚀 Key Features

The architecture is divided into four specialized autonomous agents:

### 📡 Agent 1: Data Logger Specialist
Monitors granular system events to build a comprehensive multidimensional usage profile ($\vec{p}$).
- **Battery Analytics:** Leverages `BatteryManager` for precise Level, Status, and Temperature tracking.
- **Hardware States:** Monitors `ConnectivityManager` (Wi-Fi/5G), `LocationManager` (GPS), and Bluetooth states.
- **App Usage Stats:** Utilizes `UsageStatsManager` to extract the Foreground App and its Activity Standby Bucket.

### 🧠 Agent 2: Predictive Engine Architect
Encapsulates real-time mathematical modeling to estimate the remaining battery lifetime ($T$).
- **Dynamic Formula:** Computes $T = \frac{V}{\sum_{i=1}^{n} p_i \cdot B_i}$ where $V$ is battery capacity.
- **On-device AI Placeholder:** Designed to host an INT4 Quantized LSTM-Transformer TFLite model to weight state probabilities dynamically based on Agent 1's real-time data.

### 🎨 Agent 3: UI/UX Designer
A fully dynamic, visually stunning Jetpack Compose Dashboard.
- **Remaining Time Engine:** High-visibility remaining time card based on predictive data.
- **Zipf's Law App Clustering:** Custom Canvas-based visualizations illustrating app usage dominance.
- **State Distributions:** Stacked bar charts displaying real-time hardware usage parameters.

### ⚙️ Agent 4: Modern Tech Integration (Optimization)
Diagnoses patterns and optimizes base-level hardware interactions to conserve energy.
- **5GSaver Algorithm:** Simulates Next Packet Arrival Time (PPAT) prediction via Random Forest, batching background network requests using Android `WorkManager` to maximize `RRC_INACTIVE` modem sleep.
- **Intelligent Diagnostics:** Analyzes historical state vectors to classify user tendencies (e.g., *Heavy Media Consumer*, *Network Intensive*) and yields localized, actionable efficiency recommendations.

## 🛠️ Technology Stack
- **Platform:** Native Android 15 / API 35
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Background Tasks:** Android WorkManager, Foreground Services
- **Data Visualization:** Custom Jetpack Compose Canvas Charts
- **AI/ML:** TensorFlow Lite (Integration Point)
- **System APIs:** `BatteryManager`, `UsageStatsManager`, `ConnectivityManager`

## 📦 Installation & Build

1. Clone the repository:
```bash
git clone https://github.com/zoom-ai/BatteryPulseAI.git
cd BatteryPulseAI
```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Gradle will automatically download the necessary wrappers and dependencies.
4. Run the app on an emulator (API 35+) or a physical device.

*Note: The app requests advanced permissions (e.g., Usage Access, Location) upon launch to enable the Data Logger capabilities.*

## 📄 References
- *Personalized Battery Lifetime Prediction for Mobile Devices based on Usage Patterns* (2011)
- *Usage Pattern Analysis of Smartphone Users* (2011)
