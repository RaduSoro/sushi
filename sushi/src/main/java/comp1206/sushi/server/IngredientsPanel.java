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
		ingredientTabs.addTab("Ingredient control", new IngredientsControl(server));
		ingredientTabs.addTab("Ingredient adding", new IngredientAdd(server));
		add(ingredientTabs);
	}

}

class IngredientsControl extends JPanel {
	public IngredientsControl(ServerInterface server) {
		  Panel ingredientsPanel = new Panel();
		  ingredientsPanel.setLayout(new BorderLayout());
		  DefaultTableModel ingredientsTableModel = new DefaultTableModel(){
			  @Override
			  public boolean isCellEditable(int row, int column) {
				  return false;
			  }
		  };
		  ingredientsTableModel.addColumn("Ingredient name");
		  ingredientsTableModel.addColumn("Measure units");
		  ingredientsTableModel.addColumn("Supplier");
		  ingredientsTableModel.addColumn("Restock threshold");
		  ingredientsTableModel.addColumn("Restock ammount");
		  updateTable(ingredientsTableModel,server);
		  JTable ingredientsTable = new JTable(ingredientsTableModel);
		  ingredientsTable.setShowGrid(false);
		  ingredientsTable.setIntercellSpacing(new Dimension(0, 0));
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
	public IngredientAdd(ServerInterface server) {
		Panel addIngredientPanel = new Panel();
		addIngredientPanel.setLayout(new GridLayout(6, 2, 5, 5));
		JTextField ingredientNameText = new JTextField(15);
		String[] unitCombos = {"grams", "ounces"};
		JComboBox<String> unitDropDown = new JComboBox<String>(unitCombos);
		JComboBox<Supplier> supplierDropDown = new JComboBox<Supplier>();
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


	}

	private void supplierPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Supplier supplier : server.getSuppliers()) {
			box.addItem(supplier);
		}
	}
}