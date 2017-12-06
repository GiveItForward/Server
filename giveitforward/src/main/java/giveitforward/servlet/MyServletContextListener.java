//package giveitforward.servlet;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.AnnotationConfiguration;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//public class MyServletContextListener implements ServletContextListener {
//
//  public void contextDestroyed(ServletContextEvent event) {
//    //Notification that the servlet context is about to be shut down.
//    System.out.println("HELLLLO");
//    SessionFactory factory = (SessionFactory) event.getServletContext().getAttribute("sessionFactory");
//    factory.close();
//  }
//
//  public void contextInitialized(ServletContextEvent event) {
//    // do all the tasks that you need to perform just after the server starts
//
//    //Notification that the web application initialization process is starting
//    SessionFactory factory = new AnnotationConfiguration().configure().buildSessionFactory();
//    event.getServletContext().setAttribute("sessionFactory", factory);
//  }
//
//}