package org.vitorsilvalima.jmstester.producer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSMProducerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("jms_producer.html").include(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String connectiondFactoryJNDI= req.getParameter("connectionFactoryJNDI");
		String destinationJNDI= req.getParameter("destinationJNDI");
		int nMessages= Integer.parseInt(req.getParameter("nMessages"));
		PrintWriter out = resp.getWriter();
		Context context = null;	
		ConnectionFactory connectionFactory= null;
		Destination destination = null;
		//try to find the connection factory
		try {
			context = new InitialContext();
			connectionFactory =(ConnectionFactory) context.lookup(connectiondFactoryJNDI);
			out.print("<p>connection found!!!</p>");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			out.print("<p>Error while trying to find connection factory!!! "+e.getMessage()+"</p>");
		}
		//try to find destination
		try {
			destination=(Destination)context.lookup(destinationJNDI);
			out.print("<p>destination found!!!</p>");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			out.print("<p>Error while trying to find destination!!! "+e.getMessage()+"</p>");
		}
		
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			for(;nMessages>0;nMessages--)
			{
				message.setText("Oi"+nMessages);
				producer.send(message);
			}
			producer.close();
			connection.close();
			out.print("<h1>All messages were successfully sent!!!</h1>");
		}catch(Exception e) {
			out.println("<p>An exception occurred while sending test messages: " + e.getMessage() + "</p>");
		}
		out.close();
	}

}
