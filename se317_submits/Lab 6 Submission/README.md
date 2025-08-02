# 💳 SE 3170 – Lab 6: Testing Distributed Systems

## 📋 Overview

This project implements a distributed banking system consisting of:
- A **Checking Account** (with deposits, withdrawals, transfers, and bill payments)
- A **Saving Account** (with deposit and limited transfer features)
- A **Utility Company System** (for creating accounts and managing bills)

Users interact with the system through a **Command-Line Interface (CLI)**, simulating an ATM machine. All data is persisted across sessions using Java serialization.

---

## 🏗️ Project Structure

```
/src/lab_6/
├── ATM.java                // Main CLI program
├── checkingAcc.java        // Checking account class
├── savingAcc.java          // Saving account class
├── utilityComp.java        // Utility system
├── utilityUser.java        // Utility account object
├── Bill.java               // Bill structure
├── dataManager.java        // Save/load logic

/tests/lab_6/
├── checkingAccTest.java
├── savingAccTest.java
├── utilityCompTest.java
├── dataManagerTest.java

/screenshots/
├── TR1_deposit.png
├── TR2_create_account.png
├── TR3_pay_bill.png
├── TR4_check_balance.png
├── TR5_fail_deposit.png
├── TR6_fail_withdraw.png
├── TR7_fail_transfer.png
...
```

---

## ▶️ How to Compile

Use any Java-supporting IDE (e.g., Eclipse) or compile manually:

```bash
javac lab_6/*.java
```

---

## 🚀 How to Run the Program

After compiling, run the `ATM` class:

```bash
java lab_6.ATM
```

---

## 🧑‍💻 CLI Menu Example

```plaintext
Choose Option:
1. Deposit to Checking
2. Withdraw from Checking
3. Transfer from Checking to Saving
4. Deposit to Saving
5. Transfer from Saving to Checking
6. Create Utility Account
7. Pay Utility Bill from Checking
8. View Utility Info
9. Check Balances
0. Exit
```

---

## 💾 Data Persistence

Account data is automatically saved to:
- `checking.ser`
- `saving.ser`
- `utility.ser`

These files will be created and updated each time the program exits successfully.
To restart the save data delete the .ser files

---

## 🧪 Testing

### ✔️ Code Testing
JUnit test cases cover:
- Valid and invalid transactions
- Daily limits and overdraft prevention
- Bill payment and account linking
- File save/load and serialization integrity

### ✔️ UI Testing
Run the program and use CLI options to perform:
- Deposits, withdrawals, transfers, utility payments
- Boundary tests (exceeding limits)
- Functional tests (valid user flows)
Screenshots provided in `/screenshots/` folder (TR1–TR11).

---

## 📸 Screenshots
Screenshots of console output can be found in `/screenshots/`.

---

## 👥 Authors

- Name: Erroll Barker
- ID: 564842330
