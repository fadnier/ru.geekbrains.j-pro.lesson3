package server;

public class SqliteAuthService implements AuthService {

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            String[] result = SqliteService.getUser(login, password);
            if(result[0].equals("ok")) {
                return result[1];
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String[] getAuthByLoginAndPassword(String login, String password) {
        try {
            String[] result = SqliteService.getUser(login, password);
            if(result[0].equals("ok")) {
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            SqliteService.insUser(login,password,nickname);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeNickname(int id, String nickname) {
        try {
            if(!SqliteService.checkNickname(nickname)) {
                SqliteService.updNickname(id,nickname);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
