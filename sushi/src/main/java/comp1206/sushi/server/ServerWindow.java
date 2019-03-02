package comp1206.sushi.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

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
		initialization();
		
	}
	
	public void initialization() {
		//Display window
				setSize(800,600);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JTabbedPane jtp = new JTabbedPane();
				
				jtp.setTabPlacement(JTabbedPane.LEFT);
				jtp.addTab("Postcodes", new PostcodesPanel());
			    jtp.addTab("Drones", new DronesPanel());
			    //this is a test
			    jtp.addTab("Staff", new StaffPanel());
			    jtp.addTab("Suppliers ", new SuppliersPanel());
			    jtp.addTab("Ingredients", new IngredientsPanel());
			    jtp.addTab("Dishes", new DishesPanel());
			    jtp.addTab("Orders ", new OrdersPanel());
			    jtp.addTab("Users ", new UserPanel());
			    add(jtp);
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
class PostcodesPanel extends JPanel {

	  public PostcodesPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class DronesPanel  extends JPanel {

	  public DronesPanel () {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}

class StaffPanel extends JPanel {

	  public StaffPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class SuppliersPanel extends JPanel {

	  public SuppliersPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class IngredientsPanel extends JPanel {

	  public IngredientsPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class DishesPanel extends JPanel {

	  public DishesPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class OrdersPanel extends JPanel {

	  public OrdersPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}
class UserPanel extends JPanel {

	  public UserPanel() {
	    JButton b1 = new JButton("New York");
	    add(b1);
	  }
	}


