package server;

public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);
    String[] getAuthByLoginAndPassword(String login, String password);
    boolean registration(String login, String password, String nickname);
    boolean changeNickname(int id, String nickname);
}
