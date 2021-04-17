package organizer.data.base.connection.utils;


import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.data.base.connection.Connection;

import java.sql.*;

import java.util.ArrayList;

public class List {
//    private static final String USER_LIST = "SELECT * from data.get_user_lists(?)";
    private static final String USER_LIST = "select l.list_description, l.owner_id from data.list l where l.owner_id = ? union SELECT l.list_description, l.owner_id FROM data.list l, data.share_list sl, data._user u WHERE sl.list_id = l.list_id AND u.user_id = sl.user_id AND u.telegram_id = ? group by list_description, l.owner_id, sl.owner_id;";
    private static final String LIST_ID = "SELECT list_id FROM data.list l, data._user u WHERE owner_id = u.telegram_id AND u.user_id = ? AND list_description = ?;";
    private static final String LIST_ITEMS = "SELECT title FROM data.list_item WHERE list_id = ? AND NOT deleted;";
    private static final String SHARED_LIST_USERS = "SELECT sl.user_id FROM data.share_list sl, data.list l, data._user u WHERE sl.owner_id = ? AND sl.list_id = l.list_id and l.list_description = ? and l.owner_id = u.telegram_id and u.user_id = ?;";
    private static final String INSERT_LIST = "{? = call data.insert_list(?, ?)}";
    private static final String DELETE_LIST = "{? = call data.delete_list(?, ?, ?)}";
    private static final String SHARE_LIST = "{? = call data.share_list(?, ?, ?)}";
    private static final String STOP_SHARE_LIST = "{? = call data.stop_share_list(?, ?, ?)}";
    private static final String ADD_ITEM_TO_LIST = "{? = call data.insert_item_list(?, ?, ?, ?)}";
    private static final String DELETE_ITEM_FROM_LIST = "{? = call data.delete_item_list(?, ?, ?, ?)}";


    public static synchronized ArrayList<String> getSharedUsers(long chatId, String owner, String descip){
        try{
            Connection connection = new Connection();
            PreparedStatement stmt = connection.getConnection().prepareStatement(SHARED_LIST_USERS);
            stmt.setLong(1, chatId);
            stmt.setString(2, descip);
            stmt.setString(3, owner);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<String> tmp = new ArrayList<>(0);
            while (res.next()) {
                tmp.add(res.getString(1));
            }
            System.out.println(tmp.size());
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized boolean shareList(long chatId, String discript, String name){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(SHARE_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, chatId);
            stmt.setString(3, discript);
            stmt.setString(4, name);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean stopShareList(long chatId, String discript, String name){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(STOP_SHARE_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, chatId);
            stmt.setString(3, discript);
            stmt.setString(4, name);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }


    public static synchronized boolean deleteList(Long user_id, String name, String description){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, name);
            stmt.setString(4, description);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean deleteItemList(Long user_id, String owner, String name_list, String title){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(DELETE_ITEM_FROM_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, owner);
            stmt.setString(4, name_list);
            stmt.setString(5, title);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized boolean insertItemList(Long user_id, String owner, String name, String title){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(ADD_ITEM_TO_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, owner);
            stmt.setString(4, name);
            stmt.setString(5, title);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static synchronized String[] getListItems(String name, String owner){
        try {
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(LIST_ID);
            stmt.setString(1, owner);
            stmt.setString(2, name);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            Integer id;
            if(res.next())
                id = res.getInt(1);
            else
                return null;
            stmt = connection.getConnection().prepareStatement(LIST_ITEMS);
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

    public static synchronized ArrayList<ArrayList<String>> userLists(long user_id){
        try{
            Connection connection = new Connection();

            PreparedStatement stmt = connection.getConnection().prepareStatement(USER_LIST);
            stmt.setLong(1, user_id);
            stmt.setLong(2, user_id);
            stmt.execute();
            ResultSet res = stmt.getResultSet();
            connection.close();

            ArrayList<ArrayList<String>> tmp = new ArrayList<>(0);
            tmp.add(new ArrayList<>(0));
            tmp.add(new ArrayList<>(0));
            while (res.next()) {
                tmp.get(0).add(res.getString(1));
                tmp.get(1).add(res.getString(2));
            }
            return tmp;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return null;
        }
    }

    public static synchronized boolean createList(Long user_id, String name){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(INSERT_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, user_id);
            stmt.setString(3, name);
            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();

            return res;
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return false;
        }
    }
}
