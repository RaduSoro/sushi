package comp1206.sushi.server;

import comp1206.sushi.common.Drone;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class DronesPanel  extends JPanel {

	  public DronesPanel (ServerInterface server) {
		  JTabbedPane tabs = new JTabbedPane();
		  tabs.setTabPlacement(JTabbedPane.LEFT);
		  tabs.addTab("Drone control", new DroneControl(server));
		  tabs.addTab("Map", new DroneMap(server));
		  
		  add(tabs);
	  }
}
class DroneControl extends JPanel{
	
	@SuppressWarnings("serial")
	public DroneControl(ServerInterface server) {
		  Panel dronePanel = new Panel();
		  dronePanel.setLayout(new BorderLayout());
		  //staffPanel.setPreferredSize(new Dimension(600, 600));
		  
		  //this is the inner layout for speed config
		  Panel configPanel = new Panel();
		  configPanel.setLayout(new FlowLayout());
		  configPanel.add(new JLabel("Modify speed: "));
		JTextField droneSpeedText = new JTextField(15);
		  configPanel.add(droneSpeedText);
		  JButton droneSpeedButton = new JButton("Submit");
		  configPanel.add(droneSpeedButton);
		  
		//this is the inner layout for addDrone 
		  Panel addDronePanel = new Panel();
		  addDronePanel.setLayout(new FlowLayout());
		  addDronePanel.add(new JLabel("     Add drone: "));
		JTextField droneAddText = new JTextField(15);
		  addDronePanel.add(droneAddText);
		  JButton droneAddButton = new JButton("Submit");
		  addDronePanel.add(droneAddButton);
		  
		//this is the inner layout for removeDrone 
		  Panel removeDronePanel = new Panel();
		  removeDronePanel.setLayout(new FlowLayout());
		  JButton removeDrone = new JButton("Remove drone");
		  removeDronePanel.add(removeDrone);
		  
		  //make the table the same size as the drones

		  DefaultTableModel droneTableModel = new DefaultTableModel(){
			    @Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
			    }
		  };
		  droneTableModel.addColumn("Drone name");
		  droneTableModel.addColumn("Drone speed");
		  droneTableModel.addColumn("Drone Status");
		  updateTable(droneTableModel,server);
		  JTable droneTable = new JTable(droneTableModel);
		  droneTable.setShowGrid(false);
		  droneTable.setIntercellSpacing(new Dimension(0, 0));
		  droneTable.setPreferredSize(new Dimension(400,400));
		  JScrollPane scrollTable = new JScrollPane(droneTable);


		  //the list render panel
		  Panel droneShowPanel = new Panel();
		  droneShowPanel.setLayout(new GridLayout(1,2,0,2));
		  //CHANGE HERE
		  droneShowPanel.add(scrollTable);
		  setPreferredSize(new Dimension(600,600));
		  
		  //main panel for the south of the gridLayout
		  Panel droneControlPanel = new Panel();
		  droneControlPanel.setLayout(new GridLayout(3,1,5,5));
		  droneControlPanel.add(configPanel);
		  droneControlPanel.add(addDronePanel);
		  droneControlPanel.add(removeDronePanel);
		  
		  dronePanel.add(droneShowPanel, BorderLayout.CENTER);
		  dronePanel.add(droneControlPanel, BorderLayout.SOUTH);
		  add(dronePanel);
		  
		  
		  //@ACTIONLISTENERS
		  removeDrone.addActionListener(buttonPressed -> {
			  if(droneTable.getSelectedRow() !=-1) {
				  Drone droneToRemove = (Drone)droneTableModel.getValueAt(droneTable.getSelectedRow(), 0);
					try {
						server.removeDrone(droneToRemove);
					} catch (UnableToDeleteException e) {
						e.printStackTrace();
					}
				updateTable(droneTableModel,server);
			  }
			});
		  
		  droneAddButton.addActionListener(buttonPressed -> {
			  //makes sure that there is text and a drone selected
			  if(!(droneAddText.getText().equals(""))) {
				  try {
					  server.addDrone(Integer.valueOf(droneAddText.getText()));
				  droneAddText.setText(null);
				  } catch (Exception e) {
						e.printStackTrace();
				  }
				  updateTable(droneTableModel,server);
			  }	
			});
		  
		  droneSpeedButton.addActionListener(buttonPressed -> {
			  if(droneSpeedText.getText()!=null && droneTable.getSelectedRow() !=-1) {
				  try {
					  Drone drone = (Drone)droneTableModel.getValueAt(droneTable.getSelectedRow(), 0);
					  drone.setSpeed(Integer.valueOf(droneSpeedText.getText()));
					  droneSpeedText.setText(null);
				  } catch (Exception e) {
						e.printStackTrace();
				  }
				  updateTable(droneTableModel,server);
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
		  for(Drone drone : server.getDrones()) {
			  Object[] droneRow = {drone, drone.getSpeed(), server.getDroneStatus(drone)};
			  tableModel.addRow(droneRow);
		  }  
	}	  
	
	/**
	 * 
	 * @param area the text field it's bounding the rule to
	 * @param maxLength @Integer the maximum number of characters
	 * @Description Sets a bound of maxLength characters on the @JTextArea
	 * @Example uptadeText(myTextField, 8);
	 */
	public void setMaxLimit(JTextArea area, int maxLength) {
		area.setDocument(new JTextFieldLimit(maxLength));
		area.setFocusTraversalKeysEnabled(true);
	}
	 
}

class DroneMap extends JPanel{
	
	public DroneMap(ServerInterface server) {
		
	}
}


