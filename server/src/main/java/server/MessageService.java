package server;

import java.util.HashMap;

public interface MessageService {
    HashMap<Integer,String> getAllMessage(int idSender);
    void addMessage(String msg, int idReceiving, int idSender);
}
