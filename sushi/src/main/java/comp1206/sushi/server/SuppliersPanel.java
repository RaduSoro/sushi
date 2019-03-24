package comp1206.sushi.server;


import comp1206.sushi.common.Supplier;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

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
//			suppliersPanel.add(supplierRemovePanel,BorderLayout.SOUTH);

			Panel supplierAddPanel = new Panel();
			supplierAddPanel.setLayout(new FlowLayout());
			supplierAddPanel.add(new JLabel("Supplier name"));
			JTextField supplierAddNameText = new JTextField(20);
			supplierAddPanel.add(supplierAddNameText);
			supplierAddPanel.add(new JLabel("Supplier postcode"));
		  	JTextField supplierAddPostcodeText = new JTextField(20);
		  	supplierAddPanel.add(supplierAddPostcodeText);
		  	JButton addSupplierButton = new JButton("Submit");
		  	supplierAddPanel.add(addSupplierButton);
//		  	suppliersPanel.add(supplierAddPanel, BorderLayout.SOUTH);

		  	Panel supplierControlPanel = new Panel();
		  	supplierControlPanel.setLayout(new GridLayout(2,1,5,5));
		  	supplierControlPanel.add(supplierRemovePanel);
		  	supplierControlPanel.add(supplierAddPanel);
		  	suppliersPanel.add(supplierControlPanel, BorderLayout.SOUTH);

			//@ACTIONLISTENERS
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