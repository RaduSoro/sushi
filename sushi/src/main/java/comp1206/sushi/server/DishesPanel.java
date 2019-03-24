package comp1206.sushi.server;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;

class DishesPanel extends JPanel {
	public DishesPanel(ServerInterface server) {
		JTabbedPane dishTabs = new JTabbedPane();
		dishTabs.setTabPlacement(JTabbedPane.LEFT);
		dishTabs.addTab("Dish control", new DishControl(server));
		dishTabs.addTab("Dish adding", new DishAdd(server));
		dishTabs.addTab("Dish recipe", new DishRecipe(server));
		add(dishTabs);
	}
}

class DishControl extends JPanel {
	public DishControl(ServerInterface server) {
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
		setPreferredSize(new Dimension(600, 600));
		  //@ACTIONLISTENERS

		dishRestockAmmountButton.addActionListener(buttonPressed -> {
			if (!(dishRestockAmmountText.getText().equals("")) && dishTable.getSelectedRow() != -1) {
				try {
					Dish dish = (Dish) dishTableModel.getValueAt(dishTable.getSelectedRow(), 0);
					dish.setRestockAmount(Integer.valueOf(dishRestockAmmountText.getText()));
					dishRestockAmmountText.setText(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				updateTable(dishTableModel, server);
			}
		});

		dishTresholdButton.addActionListener(buttonPressed -> {
			if (!(dishThresholdText.getText().equals("")) && dishTable.getSelectedRow() != -1) {
				try {
					Dish dish = (Dish) dishTableModel.getValueAt(dishTable.getSelectedRow(), 0);
					dish.setRestockThreshold(Integer.valueOf(dishThresholdText.getText()));
					dishThresholdText.setText(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				updateTable(dishTableModel, server);
			}
		});

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

class DishAdd extends JPanel {
	public DishAdd(ServerInterface server) {
		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout(10, 10));

		Panel configPanel = new Panel();
		configPanel.setLayout(new GridLayout(3, 1, 5, 5));
		configPanel.setSize(new Dimension(300, 300));

		Panel dishAddPanel = new Panel();
		dishAddPanel.setLayout(new GridLayout(5, 2, 5, 5));

		JTextField dishNameText = new JTextField(15);
		JTextField dishDescriptionText = new JTextField(15);
		JTextField dishPriceText = new JTextField(15);
		JTextField restockThresholdText = new JTextField(15);
		JTextField restockText = new JTextField(15);

		dishAddPanel.add(new JLabel("Dish name: "));
		dishAddPanel.add(dishNameText);
		dishAddPanel.add(new JLabel("Dish description: "));
		dishAddPanel.add(dishDescriptionText);
		dishAddPanel.add(new JLabel("Dish price: "));
		dishAddPanel.add(dishPriceText);
		dishAddPanel.add(new JLabel("Restock threshold: "));
		dishAddPanel.add(restockThresholdText);
		dishAddPanel.add(new JLabel("Restock ammount: "));
		dishAddPanel.add(restockText);

		Panel dishConfigCellOne = new Panel();
		dishConfigCellOne.setLayout(new GridLayout(2, 1, 5, 5));
		dishConfigCellOne.add(dishAddPanel);
		Panel submitButtonPanel = new Panel();
		JButton newDishButton = new JButton("Submit");
		submitButtonPanel.add(newDishButton);
		dishConfigCellOne.add(submitButtonPanel);
		configPanel.add(dishConfigCellOne);

		Panel dishConfigCellTwo = new Panel();
		dishConfigCellTwo.setLayout(new GridLayout(2, 1, 5, 5));
		Panel modifyRecipe = new Panel();
		JComboBox<Ingredient> ingredientList = new JComboBox<Ingredient>();
		ingredientPopulate(server, ingredientList);
		JLabel instrucitons = new JLabel("If quantity is 0 it will reomve the ingredient otherwise it will add it");
		JComboBox<Dish> dishList = new JComboBox<Dish>();
		dishListPopulate(server, dishList);
		JTextField ingredientQuantity = new JTextField(4);
		JButton changeRecipe = new JButton("Change recipe");


		modifyRecipe.add(ingredientList);
		modifyRecipe.add(dishList);
		modifyRecipe.add(ingredientQuantity);
		modifyRecipe.add(changeRecipe);

		dishConfigCellTwo.add(instrucitons);
		dishConfigCellTwo.add(modifyRecipe);
		configPanel.add(dishConfigCellTwo);
		//
		mainPanel.add(configPanel, BorderLayout.CENTER);
		add(mainPanel);
	}

	private void dishListPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Dish dish : server.getDishes()) {
			box.addItem(dish);
		}
	}

	private void ingredientPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Ingredient ingredient : server.getIngredients()) {
			box.addItem(ingredient);
		}
	}

}

class DishRecipe extends JPanel {
	public DishRecipe(ServerInterface server) {
		JComboBox<Dish> dishList = new JComboBox<Dish>();
		dishListPopulate(server, dishList);

		Panel dishConfigCellThree = new Panel();
		dishConfigCellThree.setLayout(new BorderLayout(5, 5));
		DefaultTableModel recipeTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		recipeTableModel.addColumn("Ingredient name");
		recipeTableModel.addColumn("Quantity");
		JTable recipeTable = new JTable(recipeTableModel);
		recipeTable.setShowGrid(false);

		JScrollPane recipeTableScroll = new JScrollPane(recipeTable);
		dishConfigCellThree.add(recipeTableScroll, BorderLayout.CENTER);
		dishConfigCellThree.add(dishList, BorderLayout.SOUTH);
		add(dishConfigCellThree);

		dishList.addActionListener(actionPerformed -> {
			updateTable(recipeTableModel, server, dishList);
		});
	}

	public void updateTable(DefaultTableModel tableModel, ServerInterface server, JComboBox dishList) {
		//clears the table
		tableModel.setRowCount(0);
		Dish dish = (Dish) dishList.getSelectedItem();
		Map<Ingredient, Number> map = server.getRecipe(dish);
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			Ingredient ingredient = (Ingredient) it.next();
			Object[] recipeRow = {ingredient, map.get(ingredient)};
			tableModel.addRow(recipeRow);
		}
	}

	private void dishListPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Dish dish : server.getDishes()) {
			box.addItem(dish);
		}
	}
}