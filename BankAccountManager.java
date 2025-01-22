import java.util.*;

public class BankAccountManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the initial account balance
        int balance = scanner.nextInt();

        // Read the number of operations to perform
        int operationCount = scanner.nextInt();
        scanner.nextLine(); // Consume the remaining newline character

        // Initialize structures for tracking operations and commits
        List<Transaction> transactions = new ArrayList<>();
        List<Integer> commitHistory = new ArrayList<>();
        List<Integer> readOutputs = new ArrayList<>();

        // Process each operation
        for (int i = 0; i < operationCount; i++) {
            String operation = scanner.nextLine();

            if (operation.equals("read")) {
                // Store the current balance for "read" operations
                readOutputs.add(balance);
            } else if (operation.startsWith("credit")) {
                // Add specified amount to the balance
                int amount = Integer.parseInt(operation.split(" ")[1]);
                balance += amount;
                transactions.add(new Transaction("credit", amount));
            } else if (operation.startsWith("debit")) {
                // Deduct specified amount from the balance
                int amount = Integer.parseInt(operation.split(" ")[1]);
                balance -= amount;
                transactions.add(new Transaction("debit", amount));
            } else if (operation.equals("commit")) {
                // Save the current balance to the commit history
                commitHistory.add(balance);
            } else if (operation.startsWith("abort")) {
                // Attempt to undo a specific transaction
                int transactionIndex = Integer.parseInt(operation.split(" ")[1]) - 1;
                if (transactionIndex >= 0 && transactionIndex < transactions.size()) {
                    Transaction transaction = transactions.get(transactionIndex);
                    if (transaction != null && (commitHistory.isEmpty() || transactionIndex >= commitHistory.size())) {
                        if (transaction.type.equals("credit")) {
                            balance -= transaction.amount;
                        } else if (transaction.type.equals("debit")) {
                            balance += transaction.amount;
                        }
                        transactions.set(transactionIndex, null); // Mark transaction as undone
                    }
                }
            } else if (operation.startsWith("rollback")) {
                // Revert balance to the value after a specific commit
                int commitIndex = Integer.parseInt(operation.split(" ")[1]) - 1;
                if (commitIndex >= 0 && commitIndex < commitHistory.size()) {
                    balance = commitHistory.get(commitIndex);
                }
            }
        }

        // Print all results from "read" operations
        for (int result : readOutputs) {
            System.out.println(result);
        }

        scanner.close();
    }

    // Class to represent a transaction
    static class Transaction {
        String type;
        int amount;

        Transaction(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }
    }
}
