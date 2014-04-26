package com.chat;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicInteger;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Alamgir
 * Date: 4/25/14
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatTest extends WebSocketServlet {

    private static final long serialVersionUID = 1L;

    private static final String GUEST_PREFIX = "Guest";

    private final AtomicInteger connectionIds = new AtomicInteger(0);
    private final ArrayList<ChatMessageInbound> connections =
            new ArrayList<>();

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol,
                                                   HttpServletRequest request) {
        return new ChatMessageInbound(connectionIds.incrementAndGet());
    }

    private final class ChatMessageInbound extends MessageInbound {

        private final String nickname;

        private ChatMessageInbound(int id) {
            this.nickname = GUEST_PREFIX + id;
        }

        @Override
        protected void onOpen(WsOutbound outbound) {
            connections.add(this);
            String message = String.format("* %s %s",
                                           nickname, "has joined.");
            broadcast(message);
        }

        @Override
        protected void onClose(int status) {
            connections.remove(this);
            String message = String.format("* %s %s",
                                           nickname, "has disconnected.");
            broadcast(message);
        }

        @Override
        protected void onBinaryMessage(ByteBuffer message) throws IOException {
            throw new UnsupportedOperationException(
                    "Binary message not supported.");
        }


        @Override
        protected void onTextMessage(CharBuffer message) throws IOException {
            // Never trust the client
            String filteredMessage = String.format("%s: %s",
                                                   nickname, HtmlFilter.filter(message.toString()));
            broadcast(filteredMessage);
        }

        private void broadcast(String message) {
            for (ChatMessageInbound connection : connections) {
                try {
                    CharBuffer buffer = CharBuffer.wrap(message);
                    connection.getWsOutbound().writeTextMessage(buffer);
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }
    }
}
