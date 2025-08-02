# 🧮 SE 317 – Lab 7: Scientific Calculator  
### Interactive GUI System Testing using Java Swing and MVC

---

## 📌 Project Overview

This project is a scientific calculator built in Java using the **MVC (Model-View-Controller)** design pattern. It features a full interactive **Swing-based GUI** and supports both basic and advanced mathematical operations. The calculator also implements memory functions and visual feedback for operation buttons.

---

## 🚀 Features

- ✅ **Basic Operations**: Addition, Subtraction, Multiplication, Division  
- ✅ **Advanced Functions**: Square, Square Root  
- ✅ **Memory Operations**:  
  - `M+` Add result to memory  
  - `M-` Subtract from memory  
  - `MR` Recall memory  
  - `MC` Clear memory  
- ✅ **User Input Management**:  
  - Input digits `0–9`  
  - Decimal point `.`  
  - Delete (`Del`) last digit  
  - Clear (`C`) full reset  
  - Sign toggle (`+/-`)  
- ✅ **Visual Feedback**:  
  - Operation buttons change background to **blue** when active  
  - Buttons reset visually after execution

---

## 📐 Architecture

This calculator follows the **MVC design pattern**:

| Component | Description |
|----------|-------------|
| `CalculatorModel.java` | Handles all calculations and memory logic |
| `CalculatorView.java` | Renders the GUI using Swing components |
| `CalculatorController.java` | Handles all user interactions and input logic |

---

## 🧪 Testing

### ✅ Unit Testing (Model Layer)
Implemented using JUnit to test:
- All mathematical functions
- Memory behavior
- Edge cases (e.g., divide by zero)

### ✅ GUI Testing (View + Controller)
Used **AssertJ-Swing** for automated UI testing:
- Simulates button presses
- Validates display content
- Tests memory and chaining behavior
- Handles edge and error cases

---

## ⚠️ Visual Feedback Note

While the assignment suggests using **two images per button** (idle and active), we implemented an effective **color-based visual feedback system** instead:

- Buttons turn **blue** when pressed
- This provides a clear and consistent cue for active operations

✅ This approach satisfies the intent of the specification while keeping the interface clean and accessible.

---

## 🛠 How to Run

### 💻 Prerequisites
- Java JDK 1.8+
- Eclipse (or any Java IDE)
- [AssertJ-Swing JARs](https://joel-costigliola.github.io/assertj/assertj-swing.html) for GUI test automation

### 📦 Run the Application
1. Open the project in Eclipse
2. Run `Main.java`
3. Interact with the calculator using the GUI

### 🧪 Run the Tests
- Run `CalculatorModelTest.java` for unit logic tests
- Run `CalculatorGUITest.java` for UI flow tests

---

## 📂 File Structure
/lab7
│
├── /src
│   ├── /controller
│   │   └── CalculatorController.java
│   │
│   ├── /model
│   │   └── CalculatorModel.java
│   │
│   ├── /view
│   │   └── CalculatorView.java
│   │
│   ├── /app.java
│       └── Main.java
│
├── /test
│   ├── /model
│   │   └── CalculatorModelTest.java
│   ├── /ui
│   │   └── CalculatorGUITest.java
│
├── README.md
├── /lib
│   └── (AssertJ-Swing JARs here, if needed)
│
└── /screenshots

---

## 👥 Credits

- **Student:** Erroll Barker  
- **Course:** SE 317  
- **Instructor:** Gaffar, Ashraf 
- **Lab:** 7 – Interactive GUI System Testing

---

## ✅ Final Notes

- All major and optional requirements from the lab document have been addressed
- UI, logic, and memory behavior are thoroughly tested
- Code is modular, documented, and exception-safe
