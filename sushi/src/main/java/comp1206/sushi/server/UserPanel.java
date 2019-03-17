package comp1206.sushi.server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import comp1206.sushi.common.Drone;
import comp1206.sushi.common.User;
import comp1206.sushi.common.Postcode;

class UserPanel extends JPanel {

	  public UserPanel(ServerInterface server) {
	    Panel userPanel = new Panel();
	    userPanel.setLayout(new BorderLayout());

		  DefaultTableModel userTableModel = new DefaultTableModel(){
			  @Override
			  public boolean isCellEditable(int row, int column) {
				  return false;
			  }
		  };
		  userTableModel.addColumn("User name");
		  userTableModel.addColumn("User postcode");
		  updateTable(userTableModel,server);
		  JTable userTable = new JTable(userTableModel);
		  userTable.setShowGrid(false);
		  userTable.setIntercellSpacing(new Dimension(0, 0));
		  userTable.setPreferredSize(new Dimension(400,400));
		  JScrollPane userScrollTable = new JScrollPane(userTable);


		  //the list render panel
		  userPanel.add(userScrollTable, BorderLayout.CENTER);
		  add(userPanel);

		  JButton removeUserButton = new JButton("Remove user");
		  userPanel.add(removeUserButton,BorderLayout.SOUTH);
		  //@ACTIONLISTENERS
		  removeUserButton.addActionListener(buttonPressed -> {
			  if(userTable.getSelectedRow() !=-1) {
				  User userToRemove = (User)userTableModel.getValueAt(userTable.getSelectedRow(), 0);
				  try {
					  server.removeUser(userToRemove);
				  } catch (ServerInterface.UnableToDeleteException e) {
					  e.printStackTrace();
				  }
				  updateTable(userTableModel,server);
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
		for(User user : server.getUsers()) {
			Object[] droneRow = {user, user.getPostcode()};
			tableModel.addRow(droneRow);
		}
	}
}

