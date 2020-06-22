package server;

import java.util.HashMap;

public class SqliteMessageService implements MessageService {

    @Override
    public HashMap<Integer,String> getAllMessage(int idSender) {
        try {
            HashMap<Integer,String> allMsg = SqliteService.getMsg(idSender);
            return allMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addMessage(String msg, int idReceiving, int idSender) {
        try {
            SqliteService.insMsg(idSender,idReceiving,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
