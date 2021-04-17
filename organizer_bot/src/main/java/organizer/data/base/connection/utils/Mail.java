package organizer.data.base.connection.utils;

import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.data.base.connection.Connection;

import java.sql.*;
import java.util.ArrayList;

public class Mail {

    private static final String USER_MAIL = "SELECT login FROM data._mail WHERE user_id = ?;";
    private static final String MAIL_FILTERS = "SELECT filter_text FROM data._mail_checker mc, data._mail m WHERE mc.mail_id = ?  group by filter_text";
    private static final String MAIL_ID = "SELECT mail_id FROM data._mail WHERE login = ? and user_id = ?";
    private static final String INSERT_MAIL = "{? = call data.insert_mail(?, ?, ?)}";
    private static final String DELETE_MAIL = "{? = call data.delete_mail(?, ?)}";
    private static final String INSERT_MAIL_CHECKER = "{? = call data.insert_mail_checker(?, ?, ?)}";
    private static final String UPDATE_MAIL_CHECKER = "{? = call data.update_mail_checker(?, ?)}";
    private static final String DELETE_MAIL_CHECKER = "{? = call data.delete_mail_checker(?, ?, ?)}";
    private static final String GET_ALL_MAIL_WITH_PASSWORD_AND_CHATS = "SELECT user_id, login, password, last_check FROM data._mail";

    public static synchronized ArrayList<String[]> allMails(){
        try{
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(GET_ALL_MAIL_WITH_PASSWORD_AND_CHATS);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<String[]> tmp = new ArrayList<>(0);
            while (res.next())
                tmp.add(new String[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4)});
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized String[] userMailFilter(long user_id, String mail){
        try {
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(MAIL_ID);
            stmt.setString(1, mail);
            stmt.setLong(2, user_id);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            Integer id;
            if(res.next())
                id = res.getInt(1);
            else
                return null;
            stmt = connection.getConnection().prepareStatement(MAIL_FILTERS);
            stmt.setInt(1, id);
            stmt.execute();

            res = stmt.getResultSet();
            connection.close();

            ArrayList<String> tmp = new ArrayList<>(0);
            while (res.next())
                tmp.add(res.getString(1));
            return tmp.toArray(new String[0]);

        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized boolean deleteMailChecker(Long user_id, String mail, String password){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_MAIL_CHECKER);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, mail);
            stmt.setString(4, password);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean updateMailChecker(Long user_id, String mail) {
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(UPDATE_MAIL_CHECKER);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, mail);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean insertMailChecker(Long user_id, String mail, String password){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(INSERT_MAIL_CHECKER);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, mail);
            stmt.setString(4, password);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized String[] userMails(long user_id){
        try{
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(USER_MAIL);
            stmt.setLong(1, user_id);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<String> tmp = new ArrayList<>(0);
            while (res.next())
                tmp.add(res.getString(1));
            return tmp.toArray(new String[0]);
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return new String[0];
        }
    }

    public static synchronized boolean deleteMail(Long user_id, String mail){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_MAIL);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, mail);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean insertMail(Long user_id, String mail, String password){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(INSERT_MAIL);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, mail);
            stmt.setString(4, password);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("MAIL", throwables.getMessage(), throwables);
            return false;
        }
    }
}
