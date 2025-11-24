package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import database.DatabaseManager;
import java.sql.*;
import java.util.Vector;

public class MainFrame extends JFrame {

    private JTable expenseTable, budgetTable;
    private DefaultTableModel expenseModel, budgetModel;
    private DatabaseManager db;

    public MainFrame() {
        db = new DatabaseManager();

        setTitle("Expense & Budget Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1,4,5,5));
        JButton addExpenseBtn = new JButton("Add Expense");
        JButton viewExpensesBtn = new JButton("View Expenses");
        JButton addBudgetBtn = new JButton("Add Budget");
        JButton viewBudgetsBtn = new JButton("View Budgets");
        buttonsPanel.add(addExpenseBtn);
        buttonsPanel.add(viewExpensesBtn);
        buttonsPanel.add(addBudgetBtn);
        buttonsPanel.add(viewBudgetsBtn);
        add(buttonsPanel, BorderLayout.NORTH);

        // Tables
        expenseModel = new DefaultTableModel(new String[]{"Description", "Amount", "Date"},0);
        expenseTable = new JTable(expenseModel);

        budgetModel = new DefaultTableModel(new String[]{"Category", "Allocated"},0);
        budgetTable = new JTable(budgetModel);

        JPanel tablesPanel = new JPanel(new GridLayout(2,1));
        tablesPanel.add(new JScrollPane(expenseTable));
        tablesPanel.add(new JScrollPane(budgetTable));
        add(tablesPanel, BorderLayout.CENTER);

        // Button actions
        addExpenseBtn.addActionListener(e -> addExpense());
        viewExpensesBtn.addActionListener(e -> loadExpenses());
        addBudgetBtn.addActionListener(e -> addBudget());
        viewBudgetsBtn.addActionListener(e -> loadBudgets());

        setVisible(true);
    }

    private void addExpense() {
        String desc = JOptionPane.showInputDialog("Enter description:");
        String amtStr = JOptionPane.showInputDialog("Enter amount:");
        String date = JOptionPane.showInputDialog("Enter date (yyyy-mm-dd):");
        try {
            double amount = Double.parseDouble(amtStr);
            db.insertExpense(desc, amount, date);
            JOptionPane.showMessageDialog(this, "Expense added!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
    }

    private void addBudget() {
        String category = JOptionPane.showInputDialog("Enter category:");
        String amtStr = JOptionPane.showInputDialog("Enter allocated amount:");
        try {
            double amount = Double.parseDouble(amtStr);
            db.insertBudget(category, amount);
            JOptionPane.showMessageDialog(this, "Budget added!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
    }

    private void loadExpenses() {
        try {
            expenseModel.setRowCount(0);
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT description, amount, date FROM expenses");
            while(rs.next()) {
                expenseModel.addRow(new Object[]{
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getDate("date")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadBudgets() {
        try {
            budgetModel.setRowCount(0);
            Statement stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT category, allocated FROM budgets");
            while(rs.next()) {
                budgetModel.addRow(new Object[]{
                    rs.getString("category"),
                    rs.getDouble("allocated")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}

