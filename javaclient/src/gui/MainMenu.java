package gui;

import model.Game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class MainMenu extends JPanel {
    private JButton btnJoin;
    private JButton btnCreate;
    private JButton btnDelete;
    private JButton btnHighscore;
    private JButton btnLogOut;
    private JTable table;
    private JButton btnUpdateTable;
    private JScrollPane scrollPane;

    /**
     * Create the panel.
     */
    public MainMenu() {
        setForeground(Color.BLACK);
        setLayout(null);

        btnJoin = new JButton("Join");
        btnJoin.setBounds(6, 40, 88, 29);
        add(btnJoin);

        btnCreate = new JButton("Create");
        btnCreate.setBounds(96, 40, 88, 29);
        add(btnCreate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(181, 40, 88, 29);
        add(btnDelete);

        btnHighscore = new JButton("Highscore");
        btnHighscore.setBounds(267, 40, 88, 29);
        add(btnHighscore);

        btnLogOut = new JButton("Log out");
        btnLogOut.setBounds(356, 40, 88, 29);
        add(btnLogOut);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(37, 81, 375, 182);
        add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        btnUpdateTable = new JButton("Update table");
        btnUpdateTable.setBounds(163, 265, 123, 29);
        add(btnUpdateTable);

    }

    public void addActionListener (ActionListener e){
        btnJoin.addActionListener(e);
        btnCreate.addActionListener(e);
        btnDelete.addActionListener(e);
        btnHighscore.addActionListener(e);
        btnLogOut.addActionListener(e);
        btnUpdateTable.addActionListener(e);
    }

    public JButton getBtnJoin() {
        return btnJoin;
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnHighscore() {
        return btnHighscore;
    }

    public JButton getBtnLogOut() {
        return btnLogOut;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnUpdateTable() {
        return btnUpdateTable;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setTable(JTable table) {
        this.table = table;
    }
}

