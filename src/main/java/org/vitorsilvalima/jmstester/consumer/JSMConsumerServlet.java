package org.vitorsilvalima.jmstester.consumer;


import java.io.IOException;
import java.io.PrintWriter;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class JSMConsumerServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("jms_consumer.html").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String connectiondFactoryJNDI= req.getParameter("connectionFactoryJNDI");
		String destinationJNDI= req.getParameter("destinationJNDI");
		PrintWriter out = resp.getWriter();
		Context context = null;	
		ConnectionFactory connectionFactory= null;
		Destination destination = null;
		//try to find the connection factory
		try {
			context = new InitialContext();
			connectionFactory =(ConnectionFactory) context.lookup(connectiondFactoryJNDI);
			//out.print("Connection found!!!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//out.print("Error while trying to find connection factory!!! "+e.getMessage());
		}
		//try to find destination
		try {
			destination=(Destination)context.lookup(destinationJNDI);
			//out.print("Destination found!!!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//out.print("Error while trying to find destination!!! "+e.getMessage());
		}
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(destination);
			TextMessage message = session.createTextMessage();
			message=(TextMessage) consumer.receive();
			out.print(consumer.receive().getJMSMessageID());
			
		}catch(Exception e) {
			out.println("An exception occurred while sending test messages: " + e.getMessage());
		}
		out.close();
	}
	
}
