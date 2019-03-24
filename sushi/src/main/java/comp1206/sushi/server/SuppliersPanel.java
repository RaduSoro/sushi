package comp1206.sushi.server;


import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class SuppliersPanel extends JPanel {

	  public SuppliersPanel(ServerInterface server) {
			Panel suppliersPanel = new Panel();
			suppliersPanel.setLayout(new BorderLayout());

			DefaultTableModel suppliersTableModel = new DefaultTableModel(){
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			suppliersTableModel.addColumn("Supplier name");
			suppliersTableModel.addColumn("Supplier postcode");
			updateTable(suppliersTableModel,server);
			JTable suppliersTable = new JTable(suppliersTableModel);
			suppliersTable.setShowGrid(false);
			suppliersTable.setIntercellSpacing(new Dimension(0, 0));
			suppliersTable.setPreferredSize(new Dimension(400,400));
			JScrollPane userScrollTable = new JScrollPane(suppliersTable);


			//the list render panel

			suppliersPanel.add(userScrollTable, BorderLayout.CENTER);
			add(suppliersPanel);

		  	Panel supplierRemovePanel = new Panel();
		  	supplierRemovePanel.setLayout(new FlowLayout());
			JButton removeUserButton = new JButton("Remove supplier");
			supplierRemovePanel.add(removeUserButton);


			Panel supplierAddPanel = new Panel();
			supplierAddPanel.setLayout(new FlowLayout());
			supplierAddPanel.add(new JLabel("Supplier name"));
			JTextField supplierAddNameText = new JTextField(20);
			supplierAddPanel.add(supplierAddNameText);
			supplierAddPanel.add(new JLabel("Supplier postcode"));

		  JComboBox<Postcode> postcodeDropBox = new JComboBox<Postcode>();
		  postcodePopulate(server, postcodeDropBox);
		  supplierAddPanel.add(postcodeDropBox);
		  	JButton addSupplierButton = new JButton("Add supplier");
		  	supplierAddPanel.add(addSupplierButton);

		  	Panel supplierControlPanel = new Panel();
		  	supplierControlPanel.setLayout(new GridLayout(2,1,5,5));
		  	supplierControlPanel.add(supplierAddPanel);
		  	supplierControlPanel.add(supplierRemovePanel);
		  	suppliersPanel.add(supplierControlPanel, BorderLayout.SOUTH);

			//@ACTIONLISTENERS

		  addSupplierButton.addActionListener(buttonPressed -> {
			  if (!(supplierAddNameText.getText().equals(""))) {
				  server.addSupplier(supplierAddNameText.getText(), (Postcode) postcodeDropBox.getSelectedItem());
				  supplierAddNameText.setText("");
				  postcodePopulate(server, postcodeDropBox);
				  updateTable(suppliersTableModel, server);
			  }
		  });

			removeUserButton.addActionListener(buttonPressed -> {
				if(suppliersTable.getSelectedRow() !=-1) {
					Supplier supplierToRemove = (Supplier) suppliersTableModel.getValueAt(suppliersTable.getSelectedRow(), 0);
					try {
						server.removeSupplier(supplierToRemove);
					} catch (ServerInterface.UnableToDeleteException e) {
						e.printStackTrace();
					}
					updateTable(suppliersTableModel,server);
				}
			});
		}

	private void postcodePopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Postcode postcode : server.getPostcodes()) {
			box.addItem(postcode);
		}
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
			for(Supplier supplier : server.getSuppliers()) {
				Object[] supplierRow = {supplier, supplier.getPostcode()};
				tableModel.addRow(supplierRow);
			}
		}
	}