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


		  //the list render panel

		  ingredientsPanel.add(ingredientsScrollTable, BorderLayout.CENTER);
		  add(ingredientsPanel);

		  Panel ingredientsRemovePanel = new Panel();
		  ingredientsRemovePanel.setLayout(new FlowLayout());
		  JButton removeIngredientButton = new JButton("Remove ingredient");
		  ingredientsRemovePanel.add(removeIngredientButton);


		  Panel ingredientAddPanel = new Panel();
		  ingredientAddPanel.setLayout(new FlowLayout());
		  ingredientAddPanel.add(new JLabel("Supplier name"));
		  JTextField supplierAddNameText = new JTextField(20);
		  ingredientAddPanel.add(supplierAddNameText);
		  ingredientAddPanel.add(new JLabel("Supplier postcode"));
		  JTextField supplierAddPostcodeText = new JTextField(20);
		  ingredientAddPanel.add(supplierAddPostcodeText);
		  JButton ingredientAddButton = new JButton("Submit");
		  ingredientAddPanel.add(ingredientAddButton);


		  Panel ingredientControlPanel = new Panel();
		  ingredientControlPanel.setLayout(new GridLayout(2,1,5,5));
		  ingredientControlPanel.add(ingredientsRemovePanel);
		  ingredientControlPanel.add(ingredientAddPanel);
		  ingredientsPanel.add(ingredientControlPanel, BorderLayout.SOUTH);

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