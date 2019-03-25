package comp1206.sushi.server;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class IngredientsPanel extends JPanel {
	public IngredientsPanel(ServerInterface server) {
		JTabbedPane ingredientTabs = new JTabbedPane();
		ingredientTabs.setTabPlacement(JTabbedPane.LEFT);
		IngredientsControl ic = new IngredientsControl(server);
		IngredientAdd ia = new IngredientAdd(server);
		ingredientTabs.addTab("Ingredient control", ic);
		ingredientTabs.addTab("Ingredient adding", ia);
		ingredientTabs.addChangeListener(ChangeListener -> {
			ic.updateTable(ic.ingredientsTableModel, server);
			ia.supplierPopulate(server, ia.supplierDropDown);
		});
		add(ingredientTabs);
	}
}

class IngredientsControl extends JPanel {
	public DefaultTableModel ingredientsTableModel = new DefaultTableModel() {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	public IngredientsControl(ServerInterface server) {
		  Panel ingredientsPanel = new Panel();
		  ingredientsPanel.setLayout(new BorderLayout());

		  ingredientsTableModel.addColumn("Ingredient name");
		  ingredientsTableModel.addColumn("Measure units");
		  ingredientsTableModel.addColumn("Supplier");
		  ingredientsTableModel.addColumn("Restock threshold");
		  ingredientsTableModel.addColumn("Restock ammount");
		  updateTable(ingredientsTableModel,server);
		  JTable ingredientsTable = new JTable(ingredientsTableModel);
		  ingredientsTable.setShowGrid(false);
		  ingredientsTable.setPreferredSize(new Dimension(400,400));
		  JScrollPane ingredientsScrollTable = new JScrollPane(ingredientsTable);

		  Panel ingredientRemovePanel = new Panel();
		  ingredientRemovePanel.setLayout(new FlowLayout());
		  JButton removeIngredientButton = new JButton("Remove ingredient");
		  ingredientRemovePanel.add(removeIngredientButton);
		  //the list render panel

		  ingredientsPanel.add(ingredientsScrollTable, BorderLayout.CENTER);

		  /**
		   * first panel controls the Restock threshold.
		   */
		  Panel ingredientsRestockThresholdPanel = new Panel();
		  ingredientsRestockThresholdPanel.setLayout(new FlowLayout());
		  ingredientsRestockThresholdPanel.add(new JLabel("Restock threshold"));
		  JTextField ingedientsThresholdText = new JTextField(5);
		  ingredientsRestockThresholdPanel.add(ingedientsThresholdText);
		  JButton ingredientTresholdButton = new JButton("Submit");
		  ingredientsRestockThresholdPanel.add(ingredientTresholdButton);

		  /**
		   * 2nd panel controls the restock ammount
		   */
		  Panel ingredientsRestockAmountPanel = new Panel();
		  ingredientsRestockAmountPanel.setLayout(new FlowLayout());
		  ingredientsRestockAmountPanel.add(new JLabel("Restock amount   "));
		  JTextField ingredientsRestockAmmountText = new JTextField(5);
		  ingredientsRestockAmountPanel.add(ingredientsRestockAmmountText);
		  JButton ingredientRestockAmmountButton = new JButton("Submit");
		  ingredientsRestockAmountPanel.add(ingredientRestockAmmountButton);


		  Panel ingredientControlPanel = new Panel();
		  ingredientControlPanel.setLayout(new GridLayout(3,1,5,5));

		  ingredientControlPanel.add(ingredientsRestockAmountPanel);
		  ingredientControlPanel.add(ingredientsRestockThresholdPanel);
		  ingredientControlPanel.add(ingredientRemovePanel);
		  ingredientsPanel.add(ingredientControlPanel, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(600, 600));
		  add(ingredientsPanel);

		  //@ACTIONLISTENERS
		ingredientTresholdButton.addActionListener(buttonPressed -> {
			if (ingredientsTable.getSelectedRow() != -1 && (!(ingedientsThresholdText.getText().equals("")))) {
				Ingredient ingredient = (Ingredient) ingredientsTableModel.getValueAt(ingredientsTable.getSelectedRow(), 0);
				ingredient.setRestockThreshold(Integer.valueOf(ingedientsThresholdText.getText()));
				ingedientsThresholdText.setText("");
				updateTable(ingredientsTableModel, server);
			}
		});

		ingredientRestockAmmountButton.addActionListener(buttonPressed -> {
			if (ingredientsTable.getSelectedRow() != -1 && (!(ingredientsRestockAmmountText.getText().equals("")))) {
				Ingredient ingredient = (Ingredient) ingredientsTableModel.getValueAt(ingredientsTable.getSelectedRow(), 0);
				ingredient.setRestockAmount(Integer.valueOf(ingredientsRestockAmmountText.getText()));
				ingredientsRestockAmmountText.setText("");
				updateTable(ingredientsTableModel, server);
			}
		});


		removeIngredientButton.addActionListener(buttonPressed -> {
			  if(ingredientsTable.getSelectedRow() !=-1) {
				  Ingredient ingredientToRemove = (Ingredient) ingredientsTableModel.getValueAt(ingredientsTable.getSelectedRow(), 0);
				  try {
					  server.removeIngredient(ingredientToRemove);
				  } catch (ServerInterface.UnableToDeleteException e) {
					  e.printStackTrace();
				  }
				  updateTable(ingredientsTableModel,server);
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
		for(Ingredient ingredient : server.getIngredients()) {
			Object[] ingredientRow = {ingredient , ingredient.getUnit(), ingredient.getSupplier(), ingredient.getRestockThreshold(), ingredient.getRestockAmount()};
			tableModel.addRow(ingredientRow);
		}
	}
}

class IngredientAdd extends JPanel {
	public JComboBox<Supplier> supplierDropDown = new JComboBox<Supplier>();
	private String[] unitCombos = {"grams", "ounces"};
	public JComboBox<String> unitDropDown = new JComboBox<String>(unitCombos);
	public IngredientAdd(ServerInterface server) {
		Panel addIngredientPanel = new Panel();
		addIngredientPanel.setLayout(new GridLayout(6, 2, 5, 5));
		JTextField ingredientNameText = new JTextField(15);

		supplierPopulate(server, supplierDropDown);
		JTextField ingredientRestockAmount = new JTextField(4);
		JTextField ingredientRestockThreshold = new JTextField(4);

		//adding to the panel
		addIngredientPanel.add(new JLabel("Ingredient name: "));
		addIngredientPanel.add(ingredientNameText);
		addIngredientPanel.add(new JLabel("Unit: "));
		addIngredientPanel.add(unitDropDown);
		addIngredientPanel.add(new JLabel("Supplier: "));
		addIngredientPanel.add(supplierDropDown);
		addIngredientPanel.add(new JLabel("Restock threshold: "));
		addIngredientPanel.add(ingredientRestockThreshold);
		addIngredientPanel.add(new JLabel("Restock amount: "));
		addIngredientPanel.add(ingredientRestockAmount);
		Panel submitPanel = new Panel();
		JButton submitButton = new JButton("Submit");
		submitPanel.add(submitButton);
		addIngredientPanel.add(submitPanel);
		add(addIngredientPanel);

		submitButton.addActionListener(buttonPressed -> {
			if (ingredientNameText.getText().equals("")) return;
			if (ingredientRestockAmount.getText().equals("")) return;
			if (ingredientRestockThreshold.getText().equals("")) return;
			server.addIngredient(ingredientNameText.getText(), (String) unitDropDown.getSelectedItem(),
					(Supplier) supplierDropDown.getSelectedItem(),
					Integer.valueOf(ingredientRestockThreshold.getText()),
					Integer.valueOf(ingredientRestockAmount.getText()));
			ingredientNameText.setText("");
			ingredientRestockAmount.setText("");
			ingredientRestockThreshold.setText("");
		});


	}

	public void supplierPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Supplier supplier : server.getSuppliers()) {
			box.addItem(supplier);
		}
	}
}