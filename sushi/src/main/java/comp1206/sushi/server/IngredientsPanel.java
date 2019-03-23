package comp1206.sushi.server;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class IngredientsPanel extends JPanel {

	  public IngredientsPanel(ServerInterface server) {
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