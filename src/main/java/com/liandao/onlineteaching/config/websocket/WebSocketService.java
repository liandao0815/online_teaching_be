package com.liandao.onlineteaching.config.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/webSocket/{roomId}/{userId}")
@Component
public class WebSocketService {
    private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, WebSocketService>> roomList
            = new ConcurrentHashMap<>();

    private Session session;
    private int roomId;
    private int userId;

    @OnOpen
    public void onOpen(@PathParam("roomId") int roomId, @PathParam("userId") int userId, Session session) {
        this.session = session;
        this.roomId = roomId;
        this.userId = userId;
        this.joinRoom(roomId, userId);
    }

    @OnMessage
    public void onMessage(String message) {
        ConcurrentHashMap<Integer, WebSocketService> room = roomList.get(roomId);

        for(Integer item: room.keySet()) {
            room.get(item).sendMessage(message);
        }
    }

    @OnClose
    public void onClose() {
        ConcurrentHashMap<Integer, WebSocketService> room = roomList.get(roomId);
        room.remove(userId);
    }


    private void joinRoom(int roomId, int userId) {
        if(!roomList.containsKey(roomId)) {
            ConcurrentHashMap<Integer, WebSocketService> room = new ConcurrentHashMap<>();
            room.put(userId, this);
            roomList.put(roomId, room);
        } else {
            ConcurrentHashMap<Integer, WebSocketService> room = roomList.get(roomId);
            if(!room.containsKey(userId)) room.put(userId, this);
        }
    }

    private void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}
