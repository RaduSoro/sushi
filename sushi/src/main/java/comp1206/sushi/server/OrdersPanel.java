package comp1206.sushi.server;

import comp1206.sushi.common.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class OrdersPanel extends JPanel {

	public OrdersPanel(ServerInterface server) {
		Panel oredersPanel = new Panel();
		oredersPanel.setLayout(new BorderLayout());

		DefaultTableModel orderTableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		orderTableModel.addColumn("Order name");
		orderTableModel.addColumn("Order status");
		orderTableModel.addColumn("Order cost");
		updateTable(orderTableModel,server);
		JTable orderTable = new JTable(orderTableModel);
		orderTable.setShowGrid(false);
		orderTable.setIntercellSpacing(new Dimension(0, 0));
		orderTable.setPreferredSize(new Dimension(400,400));
		JScrollPane orderScrollTable = new JScrollPane(orderTable);

		//the list render panel

		oredersPanel.add(orderScrollTable, BorderLayout.CENTER);
		add(oredersPanel);
		Panel orderRemovePanel = new Panel();
		orderRemovePanel.setLayout(new FlowLayout());
		JButton removeOrderButton = new JButton("Remove order");
		orderRemovePanel.add(removeOrderButton);
		Panel orderControlPanel = new Panel();
		orderControlPanel.setLayout(new GridLayout(2,1,5,5));
		orderControlPanel.add(orderRemovePanel);
		oredersPanel.add(orderControlPanel, BorderLayout.SOUTH);

		//@ACTIONLISTENERS
		removeOrderButton.addActionListener(buttonPressed -> {
			if(orderTable.getSelectedRow() !=-1) {
				Order orderToRemove = (Order) orderTableModel.getValueAt(orderTable.getSelectedRow(), 0);
				try {
					server.removeOrder(orderToRemove);
				} catch (ServerInterface.UnableToDeleteException e) {
					e.printStackTrace();
				}
				updateTable(orderTableModel,server);
			}
		});
	}

	/**
	 *
	 * @param tableModel the current table model that needs updating
	 * @param server the current server Interface to fetch data from
	 * @Description deletes the current data by setting the rowCount to 0
	 * fetches new data from the server and puts it back in the model
	 */
	public void updateTable(DefaultTableModel tableModel, ServerInterface server) {
		//clears the table
		tableModel.setRowCount(0);
		for(Order order : server.getOrders()) {
			Object[] ingredientRow = {order , server.getOrderStatus(order), server.getOrderCost(order)};
			tableModel.addRow(ingredientRow);
		}
	}
}