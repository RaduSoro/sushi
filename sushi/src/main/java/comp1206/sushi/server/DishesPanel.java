package comp1206.sushi.server;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class DishesPanel extends JPanel {

	  public DishesPanel(ServerInterface server) {
		  Panel dishPanel = new Panel();
		  dishPanel.setLayout(new BorderLayout());
		  DefaultTableModel dishTableModel = new DefaultTableModel(){
			  @Override
			  public boolean isCellEditable(int row, int column) {
				  return false;
			  }
		  };
		  dishTableModel.addColumn("Dish name");
		  dishTableModel.addColumn("Dish price");
		  dishTableModel.addColumn("Restock threshold");
		  dishTableModel.addColumn("Restock ammount");
		  updateTable(dishTableModel,server);
		  JTable dishTable = new JTable(dishTableModel);
		  dishTable.setShowGrid(false);
		  dishTable.setIntercellSpacing(new Dimension(0, 0));
		  dishTable.setPreferredSize(new Dimension(400,400));
		  JScrollPane dishScrollTable = new JScrollPane(dishTable);


		  //the list render panel

		  dishPanel.add(dishScrollTable, BorderLayout.CENTER);

		  Panel dishRemovePanel = new Panel();
		  dishRemovePanel.setLayout(new FlowLayout());
		  JButton removeDishButton = new JButton("Remove dish");
		  dishRemovePanel.add(removeDishButton);
		  //the list render panel


		  /**
		   * first panel controls the Restock threshold.
		   */
		  Panel dishRestockThresholdPanel = new Panel();
		  dishRestockThresholdPanel.setLayout(new FlowLayout());
		  dishRestockThresholdPanel.add(new JLabel("Restock threshold"));
		  JTextField dishThresholdText = new JTextField(5);
		  dishRestockThresholdPanel.add(dishThresholdText);
		  JButton dishTresholdButton = new JButton("Submit");
		  dishRestockThresholdPanel.add(dishTresholdButton);

		  /**
		   * 2nd panel controls the restock ammount
		   */
		  Panel dishRestockAmountPanel = new Panel();
		  dishRestockAmountPanel.setLayout(new FlowLayout());
		  dishRestockAmountPanel.add(new JLabel("Restock amount   "));
		  JTextField dishRestockAmmountText = new JTextField(5);
		  dishRestockAmountPanel.add(dishRestockAmmountText);
		  JButton dishRestockAmmountButton = new JButton("Submit");
		  dishRestockAmountPanel.add(dishRestockAmmountButton);


		  Panel dishControlPanel = new Panel();
		  dishControlPanel.setLayout(new GridLayout(3,1,1,1));

		  dishControlPanel.add(dishRestockAmountPanel);
		  dishControlPanel.add(dishRestockThresholdPanel);
		  dishControlPanel.add(dishRemovePanel);
		  dishPanel.add(dishControlPanel, BorderLayout.SOUTH);


		  add(dishPanel);

		  //@ACTIONLISTENERS
		  removeDishButton.addActionListener(buttonPressed -> {
			  if(dishTable.getSelectedRow() !=-1) {
				  Dish dishToRemove = (Dish) dishTableModel.getValueAt(dishTable.getSelectedRow(), 0);
				  try {
					  server.removeDish(dishToRemove);
				  } catch (ServerInterface.UnableToDeleteException e) {
					  e.printStackTrace();
				  }
				  updateTable(dishTableModel,server);
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
		for(Dish dish : server.getDishes()) {
			Object[] ingredientRow = {dish , dish.getPrice(), dish.getRestockThreshold(), dish.getRestockAmount()};
			tableModel.addRow(ingredientRow);
		}
	}
}