package comp1206.sushi.server;

import comp1206.sushi.mock.MockServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class RemoveStaff {
    MockServer server = new MockServer();
    private JPanel RemoveStaffPanel;
    private JButton DeleteSupplier;
    private JButton ShowSuppliers;
    private JList list1;

    public RemoveStaff() {
        DeleteSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        ShowSuppliers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list1.setListData((Vector) server.getSuppliers());
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
