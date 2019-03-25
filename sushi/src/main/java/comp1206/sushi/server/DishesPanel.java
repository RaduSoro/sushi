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
		DishControl dishControl = new DishControl(server);
		DishAdd dishAdd = new DishAdd(server);
		DishRecipe dishRecipe = new DishRecipe(server);
		dishTabs.addTab("Dish control", dishControl);
		dishTabs.addTab("Dish adding", dishAdd);
		dishTabs.addTab("Dish recipe", dishRecipe);
		add(dishTabs);
		dishTabs.addChangeListener(ChangeListener -> {
			dishControl.updateTable(dishControl.dishTableModel, server);
			dishAdd.dishListPopulate(server, dishAdd.dishList);
			dishAdd.ingredientPopulate(server, dishAdd.ingredientList);
			dishRecipe.dishListPopulate(server, dishRecipe.dishList);
		});
	}
}

class DishControl extends JPanel {
	public DefaultTableModel dishTableModel = new DefaultTableModel() {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	public DishControl(ServerInterface server) {
		  Panel dishPanel = new Panel();
		  dishPanel.setLayout(new BorderLayout());

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
	public JComboBox<Ingredient> ingredientList = new JComboBox<Ingredient>();
	public JComboBox<Dish> dishList = new JComboBox<Dish>();
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
		ingredientPopulate(server, ingredientList);
		JLabel instrucitons = new JLabel("If quantity is 0 it will reomve the ingredient otherwise it will add it");
		dishListPopulate(server, dishList);
		JTextField ingredientQuantity = new JTextField(4);
		JButton changeRecipe = new JButton("Change recipe");
		//action listeners

		newDishButton.addActionListener(buttonPressed -> {
			if (dishNameText.getText().equals("")) return;
			if (dishDescriptionText.getText().equals("")) return;
			if (dishPriceText.getText().equals("")) return;
			if (restockThresholdText.getText().equals("")) return;
			if (restockText.getText().equals("")) return;
			server.addDish(dishNameText.getText(), dishDescriptionText.getText(),
					Integer.valueOf(dishPriceText.getText()),
					Integer.valueOf(restockThresholdText.getText()),
					Integer.valueOf(restockText.getText()));
			dishNameText.setText("");
			dishDescriptionText.setText("");
			dishPriceText.setText("");
			restockThresholdText.setText("");
			restockText.setText("");
		});

		changeRecipe.addActionListener(buttonPressed -> {
			if (ingredientQuantity.getText().equals("")) return;
			server.addIngredientToDish((Dish) dishList.getSelectedItem(), (Ingredient) ingredientList.getSelectedItem(), Integer.valueOf(ingredientQuantity.getText()));
			ingredientQuantity.setText("");
			dishListPopulate(server, dishList);
			ingredientPopulate(server, ingredientList);
		});

		modifyRecipe.add(ingredientList);
		modifyRecipe.add(dishList);
		modifyRecipe.add(new JLabel("quantity"));
		modifyRecipe.add(ingredientQuantity);
		modifyRecipe.add(changeRecipe);

		dishConfigCellTwo.add(instrucitons);
		dishConfigCellTwo.add(modifyRecipe);
		configPanel.add(dishConfigCellTwo);
		//
		mainPanel.add(configPanel, BorderLayout.CENTER);
		add(mainPanel);
	}

	public void dishListPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Dish dish : server.getDishes()) {
			box.addItem(dish);
		}
	}

	public void ingredientPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Ingredient ingredient : server.getIngredients()) {
			box.addItem(ingredient);
		}
	}

}

class DishRecipe extends JPanel {
	public JComboBox<Dish> dishList = new JComboBox<Dish>();
	public DishRecipe(ServerInterface server) {

		Panel dishRecipePanel = new Panel();
		dishRecipePanel.setLayout(new BorderLayout(5, 5));
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
		dishRecipePanel.add(recipeTableScroll, BorderLayout.CENTER);
		dishRecipePanel.add(dishList, BorderLayout.SOUTH);
		add(dishRecipePanel);

		dishList.addActionListener(actionPerformed -> {
			try {
				updateTable(recipeTableModel, server, dishList);
			} catch (Exception e) {
				//literally do nothing.
			}

		});
		dishListPopulate(server, dishList);
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

	public void dishListPopulate(ServerInterface server, JComboBox box) {
		box.removeAllItems();
		for (Dish dish : server.getDishes()) {
			box.addItem(dish);
		}
	}
}