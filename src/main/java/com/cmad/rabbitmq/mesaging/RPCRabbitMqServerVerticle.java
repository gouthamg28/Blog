package com.cmad.rabbitmq.mesaging;
import java.io.IOException;

import com.cmad.model.dto.AuthData;
import com.cmad.service.MainVerticle;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;

public class RPCRabbitMqServerVerticle extends AbstractVerticle{

  private static final String RPC_QUEUE_NAME = "rpc_queue_blog_auth";

  private String validateUser(AuthData inputAuthData) {
//	  System.out.println("RPCRabbitMqServerVerticle.validateUser() inputAuthData = "+inputAuthData);
	  String replyMsg = "notValid";
	  if(inputAuthData!=null)	{
		  boolean flag = MainVerticle.validateToken(inputAuthData.getUserId(), inputAuthData.getUserToken());
		  System.out.println("RPCRabbitMqServerVerticle.validateUser() flag = "+flag);
		  if(flag)	{
			  replyMsg = "valid";
		  }
	  }
	  System.out.println("RPCRabbitMqServerVerticle.validateUser() replyMsg = "+replyMsg);
	  return replyMsg;
  }
  
	public void start(Future<Void> future) throws Exception {
		startRPCRabbitMqServer();
	}

	private void startRPCRabbitMqServer() {
		
		/////
//		MainVerticle.vertx.executeBlocking(blockingCodeHandler, resultHandler);
		/////
		System.out.println("RPCRabbitMqServer.startRPCRabbitMqServer() entered");
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");

	    Connection connection = null;
	    try {
	      connection      = factory.newConnection();
	      final Channel channel = connection.createChannel();

	      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

	      channel.basicQos(1);

	      System.out.println("RPCRabbitMqServerVerticle Awaiting RPC requests");

	      Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	          AMQP.BasicProperties replyProps = new AMQP.BasicProperties
	                  .Builder()
	                  .correlationId(properties.getCorrelationId())
	                  .build();

	          String response = "";

	          try {
	            String message = new String(body,"UTF-8");
System.out.println("RPCRabbitMqServerVerticle .handleDelivery() message from client = "+message);
				AuthData inputAuthData = Json.decodeValue(message, AuthData.class);

	            response += validateUser(inputAuthData);
	            System.out.println("RPCRabbitMqServerVerticle .handleDelivery() response is "+response);
	          }
	          catch (RuntimeException e){
	        	  System.out.println("RPCRabbitMqServerVerticle .handleDelivery() RuntimeException is "+e.toString());
	          }
	          finally {
	            channel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
System.out.println("RPCRabbitMqServerVerticle.handleDelivery() RESPONSE SENT TO CLIENT $$$$$$$$$");
	            channel.basicAck(envelope.getDeliveryTag(), false);
	          }
	        }
	      };

	      channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

	      //loop to prevent reaching finally block
	      while(true) {
	        try {
	          Thread.sleep(100);
	        } catch (InterruptedException _ignore) {}
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (connection != null)
	        try {
	          connection.close();
	        } catch (IOException _ignore) {}
	    }
	  }
}