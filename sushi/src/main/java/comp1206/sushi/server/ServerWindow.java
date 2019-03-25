package comp1206.sushi.server;

import comp1206.sushi.common.UpdateEvent;
import comp1206.sushi.common.UpdateListener;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides the Sushi Server user interface
 *
 */
public class ServerWindow extends JFrame implements UpdateListener {

	private static final long serialVersionUID = -4661566573959270000L;
	private ServerInterface server;
	
	/**
	 * Create a new server window
	 * @param server instance of server to interact with
	 */
	public ServerWindow(ServerInterface server) {
		super("Sushi Server");
		this.server = server;
		this.setTitle(server.getRestaurantName() + " Server");
		server.addUpdateListener(this);
		initialization(server);
		
	}
	
	public void initialization(ServerInterface server) {
		//Display window
				setSize(800,600);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				//tab stuff
				JTabbedPane tabs = new JTabbedPane();
				tabs.setTabPlacement(JTabbedPane.LEFT);
		SuppliersPanel supplierPanel = new SuppliersPanel(server);
				tabs.addTab("Postcodes", new PostcodesPanel(server));
			    tabs.addTab("Drones", new DronesPanel(server));
			    tabs.addTab("Staff", new StaffPanel(server));
		tabs.addTab("Suppliers ", supplierPanel);
			    tabs.addTab("Ingredients", new IngredientsPanel(server));
			    tabs.addTab("Dishes", new DishesPanel(server));
			    tabs.addTab("Orders ", new OrdersPanel(server));
			    tabs.addTab("Users ", new UserPanel(server));
		tabs.addChangeListener(ChangeListener -> {
			if (tabs.getSelectedIndex() == 3) {
				supplierPanel.postcodePopulate(server, supplierPanel.postcodeDropBox);
			}
		});
			    add(tabs);
				setVisible(true);
				
				//Start timed updates
				startTimer();
	}
	
	/**
	 * Start the timer which updates the user interface based on the given interval to update all panels
	 */
	public void startTimer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);     
        int timeInterval = 5;
        
        scheduler.scheduleAtFixedRate(() -> refreshAll(), 0, timeInterval, TimeUnit.SECONDS);
	}
	
	/**
	 * Refresh all parts of the server application based on receiving new data, calling the server afresh
	 */
	public void refreshAll() {
		
	}

	
	@Override
	/**
	 * Respond to the model being updated by refreshing all data displays
	 */
	public void updated(UpdateEvent updateEvent) {
		refreshAll();
	}
	
}


