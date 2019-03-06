package comp1206.sushi.server;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.html.HTMLDocument.Iterator;
import javax.swing.*;

class SuppliersPanel extends JPanel {
	  public SuppliersPanel(ServerInterface server) {
		  JTabbedPane tabs = new JTabbedPane();
		  tabs.setTabPlacement(JTabbedPane.LEFT);
		  tabs.addTab("Add Suplier", new AddSuplierPanel(server));
		  tabs.addTab("Remove Suplier", new RemoveSuplierPanel(server));
//		  tabs.addTab("Search For Suplier", new SearchPanel());
		  
		  add(tabs);

	  }
	}
class AddSuplierPanel extends JPanel {

	  public AddSuplierPanel(ServerInterface server) {
		  Panel supplierAddPanel = new Panel();
		  supplierAddPanel.setLayout(new GridLayout(4,3,0,10));
		  JButton submit = new JButton("Submit");
		  
		  JTextArea supplierNameText = new JTextArea(1,25);
		  JTextArea supplierPostcodeText = new JTextArea(1,25);
//		  JTextArea ingredientText = new JTextArea(1,25);
		  
		  setMaxLimit(supplierNameText, 20);
		  setMaxLimit(supplierPostcodeText, 20);
//		  setMaxLimit(ingredientText, 20);
		
		  
		  supplierAddPanel.add(new JLabel("Name of the supplier:"));
		  supplierAddPanel.add(supplierNameText);
		  
		  supplierAddPanel.add(new JLabel("Supplier postcode:"));
		  supplierAddPanel.add(supplierPostcodeText);
		  
//		  supplierAddPanel.add(new JLabel("Ingredient Supplied"));
//		  supplierAddPanel.add(ingredientText);
		  
		  //this button should call add supplier from the MockServer.java
		  supplierAddPanel.add(submit);
		  submit.addActionListener(buttonPressed -> {
			  String supplierName = supplierNameText.getText();
			  Postcode supplierPO = new Postcode(supplierPostcodeText.getText());
			  supplierNameText.setText("");
			  supplierPostcodeText.setText("");
			  server.addSupplier(supplierName, supplierPO);
			  for(Supplier lol : server.getSuppliers()) {
			  }
		  });
		  submit.setLocation(400,400);
		  setPreferredSize(new Dimension(600,600));
		  add(supplierAddPanel);
		  
	  }
	  public void setMaxLimit(JTextArea area, int maxLength) {
			area.setDocument(new JTextFieldLimit(maxLength));
			area.setFocusTraversalKeysEnabled(true);
		 }
	}

class RemoveSuplierPanel extends JPanel {

	  public RemoveSuplierPanel(ServerInterface server) {
		  Panel supplierAddPanel = new Panel();
		  supplierAddPanel.setLayout(new GridLayout(4,3,0,10));
		  JButton submit = new JButton("Delete supplier");
		  
		  JTextArea supplierNameText = new JTextArea(1,25);
		  JTextArea supplierPostcodeText = new JTextArea(1,25);

		  supplierAddPanel.add(new JLabel("Name of the supplier:"));
		  supplierAddPanel.add(supplierNameText);
		  
		  supplierAddPanel.add(new JLabel("Supplier postcode:"));
		  supplierAddPanel.add(supplierPostcodeText);
		  
		  
		  supplierAddPanel.add(submit);
		  submit.addActionListener(buttonPressed -> {
			  String supplierName = supplierNameText.getText();
			  Postcode supplierPO = new Postcode(supplierPostcodeText.getText());
			  supplierNameText.setText("");
			  supplierPostcodeText.setText("");
			  Supplier supplierToBeRemoved = new Supplier(supplierName,supplierPO);
			  boolean found = false;

			  
			  List<Supplier> supplierList = server.getSuppliers();
			  java.util.Iterator<Supplier> itr = supplierList.iterator();
			  while(itr.hasNext()) {
				  supplierToBeRemoved = (Supplier) itr.next();
				  if(supplierToBeRemoved.getName().equals(supplierName)) {
					  found=true;
					  break;
				  }
			  }
			  if(found) {
				  try {
					server.removeSupplier(supplierToBeRemoved);
				} catch (UnableToDeleteException e) {
					
					e.printStackTrace();
				}
				  found = false;
			  }
			  for(Supplier supplier : server.getSuppliers()) {
			  }

		  });
		  setPreferredSize(new Dimension(600,600));
		  add(supplierAddPanel);
		  
	  }
	}
/**
 * 
 * @author http://www.java2s.com/Tutorial/Java/0240__Swing/LimitJTextFieldinputtoamaximumlength.htm
 *
 */
class JTextFieldLimit extends PlainDocument {
	  private int limit;
	  JTextFieldLimit(int limit) {
	    super();
	    this.limit = limit;
	  }

	  JTextFieldLimit(int limit, boolean upper) {
	    super();
	    this.limit = limit;
	  }

	  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	    if (str == null)
	      return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str, attr);
	    }
	  }
	}