package comp1206.sushi.server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import comp1206.sushi.common.Staff;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

class StaffPanel extends JPanel {

	  public StaffPanel(ServerInterface server) {
		  Panel staffPanel = new Panel();
		  staffPanel.setLayout(new GridLayout(2,1,5,2));
		  JButton submit = new JButton("Add staff");
		  JButton remove = new JButton("Remove staff");

		  
		  JTextArea addStaffText = new JTextArea(1,25);
		  JTextArea removeStaffText = new JTextArea(1,25);
		  
		  setMaxLimit(addStaffText, 8);
		  setMaxLimit(removeStaffText, 8);
		  
		  Panel staffAddPanel = new Panel();
		  staffAddPanel.setLayout(new GridLayout(3,1,0,2));
		  staffAddPanel.add(new JLabel("Add staff :"));
		  staffAddPanel.add(addStaffText);
		  staffAddPanel.add(submit);
		  
		  Panel staffRemovePanel= new Panel();
		  staffRemovePanel.setLayout(new GridLayout(3,1,0,2));
		  staffRemovePanel.add(new JLabel("Remove staff:"));
		  staffRemovePanel.add(removeStaffText);
		  staffRemovePanel.add(remove);
		  
		  Panel staffShowPanel = new Panel();
		  staffShowPanel.setLayout(new GridLayout(1,2,0,2));
		  JTextArea staffList = new JTextArea();
		  uptadeText(staffList,server);
		  staffList.setLineWrap(true);
		  staffList.setEditable(false);
		  JScrollPane listScroller = new JScrollPane(staffList);
		  listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		  listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		  staffShowPanel.add(listScroller);
		  listScroller.setPreferredSize(new Dimension(90, 199));
		  
		  submit.addActionListener(buttonPressed -> {
			  String staffName = addStaffText.getText();
			  addStaffText.setText("");
			  server.addStaff(staffName);
			  uptadeText(staffList,server);
		  });
		  
		  remove.addActionListener(buttonPressed -> {
			  String staffName = removeStaffText.getText();
			  removeStaffText.setText("");
			  for(Staff staff : server.getStaff()) {
				  if(staffName.equals(staff.toString())) {
					  try {
						server.removeStaff(staff);;
					} catch (UnableToDeleteException e) {
						e.printStackTrace();
					}
					  break;
				  }
			  }
			  uptadeText(staffList,server);

		  });
		  
		  setPreferredSize(new Dimension(400,400));
		  staffPanel.add(staffAddPanel);
		  staffPanel.add(staffRemovePanel);
		  add(staffPanel);
		  add(staffShowPanel);
	  }
	  public void uptadeText(JTextArea box, ServerInterface server) {
		  String aux = "";
		  for(Staff staff : server.getStaff()) {
			  aux = aux +(staff.toString() + "\n");
		  }
		  box.setText(aux);
	  }
	  
	  public void setMaxLimit(JTextArea area, int maxLength) {
			area.setDocument(new JTextFieldLimit(maxLength));
			area.setFocusTraversalKeysEnabled(true);
		 }
	  }
	