package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import notification.Notification;

public class Listener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		cron();
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void cron() {
		Notification notify = new Notification();
		Thread t = new Thread(notify);
		t.start();
	}

}
