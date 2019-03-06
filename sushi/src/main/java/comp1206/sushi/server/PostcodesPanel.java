package comp1206.sushi.server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

class PostcodesPanel extends JPanel {
	
	  public PostcodesPanel(ServerInterface server) {
		  Panel postcodePanel = new Panel();
		  postcodePanel.setLayout(new GridLayout(2,1,5,2));
		  JButton submit = new JButton("Add postcode");
		  JButton remove = new JButton("Remove postcode");

		  
		  JTextArea addPostcodeText = new JTextArea(1,25);
		  JTextArea removePostcodeText = new JTextArea(1,25);
		  
		  setMaxLimit(addPostcodeText, 8);
		  setMaxLimit(removePostcodeText, 8);
		  
		  Panel postcodeAddPanel = new Panel();
		  postcodeAddPanel.setLayout(new GridLayout(3,1,0,2));
		  postcodeAddPanel.add(new JLabel("Add postcode :"));
		  postcodeAddPanel.add(addPostcodeText);
		  postcodeAddPanel.add(submit);
		  
		  Panel postcodeRemovePanel = new Panel();
		  postcodeRemovePanel.setLayout(new GridLayout(3,1,0,2));
		  postcodeRemovePanel.add(new JLabel("Remove postcode:"));
		  postcodeRemovePanel.add(removePostcodeText);
		  postcodeRemovePanel.add(remove);
		  
		  Panel postcodeShowPanel = new Panel();
		  postcodeShowPanel.setLayout(new GridLayout(1,2,0,2));
		  JTextArea postalcodeList = new JTextArea();
		  uptadeText(postalcodeList,server);
		  postalcodeList.setLineWrap(true);
		  postalcodeList.setEditable(true);
		  JScrollPane listScroller = new JScrollPane(postalcodeList);
		  listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		  listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		  postcodeShowPanel.add(listScroller);
		  listScroller.setPreferredSize(new Dimension(90, 199));
		  
		  submit.addActionListener(buttonPressed -> {
			  String PO = addPostcodeText.getText();
			  addPostcodeText.setText("");
			  server.addPostcode(PO);
			  uptadeText(postalcodeList,server);
		  });
		  
		  remove.addActionListener(buttonPressed -> {
			  String postalcode = removePostcodeText.getText();
			  removePostcodeText.setText("");
			  for(Postcode PO : server.getPostcodes()) {
				  if(postalcode.equals(PO.toString())) {
					  try {
						server.removePostcode(PO);
					} catch (UnableToDeleteException e) {
						e.printStackTrace();
					}
					  break;
				  }
			  }
			  uptadeText(postalcodeList,server);

		  });
		  
		  setPreferredSize(new Dimension(400,400));
		  postcodePanel.add(postcodeAddPanel);
		  postcodePanel.add(postcodeRemovePanel);
		  add(postcodePanel);
		  add(postcodeShowPanel);
	  }
	  public void uptadeText(JTextArea box, ServerInterface server) {
		  String aux = "";
		  for(Postcode PO : server.getPostcodes()) {
			  aux = aux +(PO.toString() + "\n");
		  }
		  box.setText(aux);
	  }
	  
	  public void setMaxLimit(JTextArea area, int maxLength) {
			area.setDocument(new JTextFieldLimit(maxLength));
			area.setFocusTraversalKeysEnabled(true);
		 }
	  }
	