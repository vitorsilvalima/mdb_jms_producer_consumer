package org.vitorsilvalima.jmstester.consumer;

import java.util.Date;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

@MessageDriven(
	    activationConfig = 
	    {
	        @ActivationConfigProperty(propertyName = "destinationType",
                    propertyValue = "javax.jms.Queue"),
	        @ActivationConfigProperty(propertyName="connectionFactoryJndiName", propertyValue="java:/ConnectionFactory2"),
	        @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "true"),
	        @ActivationConfigProperty(propertyName="destination", propertyValue="java:jboss/queues/myQueue")
	    })
public class JMSListener implements MessageListener
{
	   public JMSListener() 
	   {
	   }
	   public final void onMessage(Message message){
	        try {
	            if (message instanceof TextMessage) {
	                System.out.println("Queue: I received a TextMessage at " + 
	                                new Date());
	                TextMessage msg = (TextMessage) message;
	                System.out.println("Message is : " + msg.getText());
	            } else if (message instanceof ObjectMessage) {
	                System.out.println("Queue: I received an ObjectMessage " +
	                            " at " + new Date());
	                ObjectMessage msg = (ObjectMessage) message;
	            } else {
	                System.out.println("Not valid message for this Queue MDB");
	            }
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
	    }
}
