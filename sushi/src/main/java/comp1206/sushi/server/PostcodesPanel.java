package comp1206.sushi.server;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PostcodesPanel extends JPanel {

    public PostcodesPanel(ServerInterface server) {
        Panel postcodePanel = new Panel();
        postcodePanel.setLayout(new GridLayout(2, 1, 5, 2));
        JButton submit = new JButton("Add postcode");
        JButton remove = new JButton("Remove postcode");
        JTextField addPostcodeText = new JTextField(25);


        Panel postcodeAddPanel = new Panel();
        postcodeAddPanel.setLayout(new GridLayout(3, 1, 0, 2));
        postcodeAddPanel.add(new JLabel("Add postcode :"));
        postcodeAddPanel.add(addPostcodeText);
        postcodeAddPanel.add(submit);

        Panel postcodeRemovePanel = new Panel();
        postcodeRemovePanel.setLayout(new GridLayout(4, 1, 0, 2));
        postcodeRemovePanel.add(remove);
        JTextField errorField = new JTextField(30);
        errorField.setEditable(false);
        JButton latLot = new JButton("get Lat&Lot");
        JButton distance = new JButton("get Distance");
        postcodeRemovePanel.add(latLot);
        postcodeRemovePanel.add(distance);
        postcodeRemovePanel.add(errorField);
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
         * SOURCE: https://howtodoinjava.com/regex/java-regex-validate-u-k-postal-codes-postcodes/
         */
        String regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";
        Pattern pattern = Pattern.compile(regex);
        /**
         * Adds an @ActionListener to the submit @JButton checks if the @String PO it's
         * not null and adds the post code. After that it calls the @Method updateText
         */
        submit.addActionListener(buttonPressed -> {
            String PO = addPostcodeText.getText();
            for (Postcode po : server.getPostcodes()) {
                if (po.toString().toLowerCase().equals(PO.toLowerCase())) {
                    errorField.setText("Duplicate postcode!");
                    return;
                }
            }
            if (!PO.equals("")) {
                Matcher matcher = pattern.matcher(PO);
                if (matcher.matches()) {
                    addPostcodeText.setText("");
                    server.addPostcode(PO);
                    errorField.setText(null);
                    uptadeText(model, server);
                } else {
                    errorField.setText("Invalid po!");
                }
            }
        });

        remove.addActionListener(buttonPressed -> {
            Postcode postalcode = list.getSelectedValue();
            try {
                for (Supplier supplier : server.getSuppliers()) {
                    if (supplier.getPostcode().equals(postalcode)) {
                        errorField.setText("Can't remove a PO without removing supplier");
                        return;
                    }
                }
                errorField.setText("");
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
     * @param model  the model it's updating
     * @param server instance
     * @Description Clears the model than fetches the @Postcode list from the server
     * and iterates through it adding it to the model
     * @Example uptadeText(model, server);
     */
    public void uptadeText(DefaultListModel<Postcode> model, ServerInterface server) {
        model.clear();
        for (Postcode postcode : server.getPostcodes()) {
            model.addElement(postcode);
        }
    }

}
