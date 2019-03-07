package comp1206.sushi.server;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import comp1206.sushi.common.Order;

class OrdersPanel extends JPanel {

	  public OrdersPanel(ServerInterface server) {

	    JList<Order> list = new JList<Order>();
	    DefaultListModel<Order> model = new DefaultListModel();
	    for(Order order : server.getOrders()) {
	    	model.addElement(order);
	    }
	    list.setModel(model);
	    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    System.out.println(list.getSelectedValue());
                    model.addElement(new Order());
                    model.removeElement(list.getSelectedValue());
                }
            }
        });
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        add(listScroller);
	  }
	}