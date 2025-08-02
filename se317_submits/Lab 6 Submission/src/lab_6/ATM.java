package lab_6;

import java.util.Scanner;

/**
 * ATM command-line interface for managing checking/saving accounts and utility payments.
 * Supports deposits, withdrawals, transfers, utility bill payment, and balance viewing.
 */
public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        checkingAcc checking;
        savingAcc saving;
        utilityComp utility;

        // Load persistent data if available
        try {
            checking = (checkingAcc) dataManager.loadObject("checking.ser");
            saving = (savingAcc) dataManager.loadObject("saving.ser");
            utility = (utilityComp) dataManager.loadObject("utility.ser");
            System.out.println("✅ Loaded saved data.");
        } catch (Exception e) {
            System.out.println("🆕 No saved data found. Starting fresh.");
            checking = new checkingAcc();
            saving = new savingAcc();
            utility = new utilityComp();
        }

        System.out.println("\n💳 Welcome to the ATM System!");

        while (true) {
            System.out.println("\nChoose Option:");
            System.out.println("1. Deposit to Checking");
            System.out.println("2. Withdraw from Checking");
            System.out.println("3. Transfer from Checking to Saving");
            System.out.println("4. Deposit to Saving");
            System.out.println("5. Transfer from Saving to Checking");
            System.out.println("6. Create Utility Account");
            System.out.println("7. Pay Utility Bill from Checking");
            System.out.println("8. View Utility Info");
            System.out.println("9. Check Balances");
            System.out.println("0. Exit");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("⚠️ Invalid input.");
                continue;
            }

            try {
                switch (choice) {
                    case 1: // Deposit to checking
                        System.out.print("Enter amount to deposit: ");
                        double depC = Double.parseDouble(sc.nextLine());
                        checking.deposit(depC);
                        System.out.println("✅ Deposit successful.");
                        break;
                    case 2: // Withdraw from checking
                        System.out.print("Enter amount to withdraw: ");
                        double withC = Double.parseDouble(sc.nextLine());
                        checking.withdraw(withC);
                        System.out.println("✅ Withdrawal successful.");
                        break;
                    case 3: // Transfer from checking to saving
                        System.out.print("Enter amount to transfer: ");
                        double trCS = Double.parseDouble(sc.nextLine());
                        checking.transferToSaving(saving, trCS);
                        System.out.println("✅ Transfer successful.");
                        break;
                    case 4: // Deposit to saving
                        System.out.print("Enter amount to deposit: ");
                        double depS = Double.parseDouble(sc.nextLine());
                        saving.deposit(depS);
                        System.out.println("✅ Deposit to saving successful.");
                        break;
                    case 5: // Transfer from saving to checking
                        System.out.print("Enter amount to transfer: ");
                        double trSC = Double.parseDouble(sc.nextLine());
                        saving.transferToChecking(checking, trSC);
                        System.out.println("✅ Transfer to checking successful.");
                        break;
                    case 6: // Create utility account
                        System.out.print("Enter username: ");
                        String user = sc.nextLine();
                        System.out.print("Enter password: ");
                        String pass = sc.nextLine();
                        String accountNum = utility.createAccount(user, pass);
                        System.out.println("✅ Account created. Your account number: " + accountNum);
                        break;
                    case 7: // Pay utility bill
                        System.out.print("Enter username: ");
                        user = sc.nextLine();
                        System.out.print("Enter password: ");
                        pass = sc.nextLine();
                        System.out.print("Enter amount to pay: ");
                        double billAmt = Double.parseDouble(sc.nextLine());
                        checking.payBill(utility, user, pass, billAmt);
                        System.out.println("✅ Bill paid successfully.");
                        break;
                    case 8: // View utility info
                        System.out.print("Enter username: ");
                        user = sc.nextLine();
                        System.out.print("Enter password: ");
                        pass = sc.nextLine();
                        utility.showNextBill(user, pass);
                        utility.showPaymentHistory(user, pass);
                        break;
                    case 9: // Check balances
                        System.out.println("💰 Checking Balance: $" + checking.getBalance());
                        System.out.println("💰 Saving Balance: $" + saving.getBalance());
                        break;
                    case 0: // Save and exit
                        System.out.println("💾 Saving data and exiting...");
                        try {
                            dataManager.saveObject(checking, "checking.ser");
                            dataManager.saveObject(saving, "saving.ser");
                            dataManager.saveObject(utility, "utility.ser");
                            System.out.println("✅ Data saved.");
                        } catch (Exception e) {
                            System.out.println("❌ Failed to save data: " + e.getMessage());
                        }
                        sc.close();
                        return;
                    default:
                        System.out.println("⚠️ Invalid option.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
}