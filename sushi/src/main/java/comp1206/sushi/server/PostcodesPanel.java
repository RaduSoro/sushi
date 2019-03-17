package comp1206.sushi.server;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

class PostcodesPanel extends JPanel {

	public PostcodesPanel(ServerInterface server) {
		Panel postcodePanel = new Panel();
		postcodePanel.setLayout(new GridLayout(2, 1, 5, 2));
		JButton submit = new JButton("Add postcode");
		JButton remove = new JButton("Remove postcode");
		JTextArea addPostcodeText = new JTextArea(1, 25);

		setMaxLimit(addPostcodeText, 8);

		Panel postcodeAddPanel = new Panel();
		postcodeAddPanel.setLayout(new GridLayout(3, 1, 0, 2));
		postcodeAddPanel.add(new JLabel("Add postcode :"));
		postcodeAddPanel.add(addPostcodeText);
		postcodeAddPanel.add(submit);

		Panel postcodeRemovePanel = new Panel();
		postcodeRemovePanel.setLayout(new GridLayout(3, 1, 0, 2));
		postcodeRemovePanel.add(remove);
		remove.setToolTipText("Select post code from the list than click remove");

		Panel postcodeShowPanel = new Panel();
		JList<Postcode> list = new JList<Postcode>();
		DefaultListModel<Postcode> model = new DefaultListModel();
		for (Postcode postcode : server.getPostcodes()) {
			model.addElement(postcode);
		}
		list.setModel(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(80, 199));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		add(listScroller);
		/**
		 * Adds an @ActionListener to the submit @JButton checks if the @String PO it's
		 * not null and adds the post code. After that it calls the @Method updateText
		 */
		submit.addActionListener(buttonPressed -> {
			String PO = addPostcodeText.getText();
			if (!PO.equals("")) {
				addPostcodeText.setText("");
				server.addPostcode(PO);
				uptadeText(model, server);
			}
		});

		remove.addActionListener(buttonPressed -> {
			Postcode postalcode = list.getSelectedValue();
			try {
				server.removePostcode(postalcode);
			} catch (UnableToDeleteException e) {
				e.printStackTrace();
			}
			uptadeText(model, server);
		});

		postcodePanel.add(postcodeAddPanel);
		postcodePanel.add(postcodeRemovePanel);
		add(postcodePanel);
		add(postcodeShowPanel);
	}

	/**
	 * 
	 * @param model  the model it's updating
	 * @param server instance
	 * @Description Clears the model than fetches the @Postcode list from the server
	 *              and iterates through it adding it to the model
	 * @Example uptadeText(model, server);
	 */
	public void uptadeText(DefaultListModel<Postcode> model, ServerInterface server) {
		model.clear();
		for (Postcode postcode : server.getPostcodes()) {
			model.addElement(postcode);
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
