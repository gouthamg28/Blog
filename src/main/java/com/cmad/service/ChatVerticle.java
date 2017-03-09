package com.cmad.service;

import java.util.Vector;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;


public class ChatVerticle extends AbstractVerticle {
	
	static Vector<BufferHandler> webSocketHandlers = new Vector();

	public void start(Future<Void> future) throws Exception {
//		startRandomNumberServer();
	}

//	public static void main(String[] args) {
//		startRandomNumberServer();
//	}
	
	public static void publishMessageToClients(String message) {
		if (webSocketHandlers != null && webSocketHandlers.size() > 0) 
		{
			for (BufferHandler wsHandler : webSocketHandlers) 
			{
				wsHandler.getWebSocket().writeFinalTextFrame(message);
			}
		}
	}
}

class CustomWebSocketHandler<E> implements Handler<E> {
	
	public void handle(E event) {
		ServerWebSocket ws = (ServerWebSocket) event;
		if (ws.path().equals("/chat")) {

			BufferHandler<Buffer> customHandler = new BufferHandler<Buffer>(ws);
			ws.handler(customHandler);
			ChatVerticle.webSocketHandlers.add(customHandler);
			System.out.println("WebSocketHandler.handle() webSocketHandlers.size() = "+ChatVerticle.webSocketHandlers.size());
			
			ws.closeHandler(new Handler<Void>() {
				@Override
				public void handle(final Void event) {
					ChatVerticle.webSocketHandlers.remove(customHandler);
				}
			});
		} else {
			ws.reject();
		}
	}
}

class BufferHandler<E> implements Handler<E>	{
	
	ServerWebSocket ws;
	int[] intArr;
	String message;
	static int counter = 0;
	
	public BufferHandler(ServerWebSocket webSocket) {
		this.ws = webSocket;
	}
	
	public ServerWebSocket getWebSocket() {
		return ws;
	}
	
	public void handle(E event) {
		System.out.println("BufferHandler.handle() event = "+event.getClass());
		System.out.println("BufferHandler.handle() ((Buffer)event).toString() = "+((Buffer)event).toString());

		message = ((Buffer)event).toString();
		counter++;
		ChatVerticle.publishMessageToClients(" MsgNumber: "+counter+" "+message);
	}
}