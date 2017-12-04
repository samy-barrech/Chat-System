package com.langram.utils.exchange.network;

import java.io.IOException;

public interface MessageReceiver {
    void listenOnPort(String ipAdress, int port, IncomingMessageListener messageListener) throws IOException;
}