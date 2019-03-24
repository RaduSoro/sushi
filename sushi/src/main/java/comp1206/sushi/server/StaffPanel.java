package comp1206.sushi.server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Staff;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

class StaffPanel extends JPanel {

	  public StaffPanel(ServerInterface server) {
		  Panel staffPanel = new Panel();
		  staffPanel.setLayout(new GridLayout(2,1,5,2));
		  JButton submit = new JButton("Add staff");
		  JButton remove = new JButton("Remove staff");
		  JButton status = new JButton("Get staff status");

		  
		  JTextArea addStaffText = new JTextArea(1,25);
		  JTextArea staffStatus = new JTextArea(1,25);
		  staffStatus.setEditable(false);
		  
		  setMaxLimit(addStaffText, 8);
		  setMaxLimit(staffStatus, 8);
		  
		  Panel staffAddPanel = new Panel();
		  staffAddPanel.setLayout(new GridLayout(3,1,0,2));
		  staffAddPanel.add(new JLabel("Add staff :"));
		  staffAddPanel.add(addStaffText);
		  staffAddPanel.add(submit);
		  
		  Panel staffRemovePanel= new Panel();
		  staffRemovePanel.setLayout(new GridLayout(4,1,0,2));
		  staffRemovePanel.add(remove);
		  staffRemovePanel.add(status);
		  staffRemovePanel.add(staffStatus);
		  
		  Panel staffShowPanel = new Panel();
		  staffShowPanel.setLayout(new GridLayout(1,2,0,2));
		  JList<Staff> list = new JList<Staff>();
		  DefaultListModel<Staff> model = new DefaultListModel();
		  uptadeText(model,server);
		  list.setModel(model);
		  list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		  list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		  JScrollPane listScroller = new JScrollPane(list);
		  listScroller.setPreferredSize(new Dimension(80, 199));
		  listScroller.setAlignmentX(LEFT_ALIGNMENT);
		  staffShowPanel.add(listScroller);
		  
		  
		  status.addActionListener(buttonPressed -> {
			  staffStatus.setText(server.getStaffStatus(list.getSelectedValue()));
		  });
		  
		  submit.addActionListener(buttonPressed -> {
			  String staffName = addStaffText.getText();
			  if(!staffName.equals("")){
				  addStaffText.setText("");
				  server.addStaff(staffName);
			  }

			  uptadeText(model, server);
		  });
		  
		  remove.addActionListener(buttonPressed -> {
			  Staff staffToRemove = list.getSelectedValue();
				try {
					server.removeStaff(staffToRemove);
				} catch (UnableToDeleteException e) {
					e.printStackTrace();
				}
				uptadeText(model, server);
			});
		  
		  setPreferredSize(new Dimension(400,400));
		  staffPanel.add(staffAddPanel);
		  staffPanel.add(staffRemovePanel);
		  add(staffPanel);
		  add(staffShowPanel);
	  }
	  
		/**
		 * 
		 * @param model  the model it's updating
		 * @param server instance
		 * @Description Clears the model than fetches list from the server
		 * and iterates through it adding it to the model
		 * @Example uptadeText(model, server);
		 */
		public void uptadeText(DefaultListModel<Staff> model, ServerInterface server) {
			model.clear();
			for (Staff staff : server.getStaff()) {
				model.addElement(staff);
			}
		}

		/**
		 * 
		 * @param area      the text field it's bounding the rule to
		 * @param maxLength @Integer the maximum number of characters
		 * @Description Sets a bound of maxLength characters on the @JTextArea
		 * @Example uptadeText(myTextField, 8);
		 */
		public void setMaxLimit(JTextArea area, int maxLength) {
			area.setDocument(new JTextFieldLimit(maxLength));
			area.setFocusTraversalKeysEnabled(true);
		}
	}