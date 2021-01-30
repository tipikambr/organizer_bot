package organizer.data.base.connection.utils;


import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.bot.utils.menus.MainMenu;
import organizer.data.base.connection.Connection;
import java.sql.Types;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class List {
    private static final String USER_LIST = "{? = call data.get_user_list(?)}";
    private static final String INSERT_LIST = "{? = call data.insert_list(?, ?)}";


    public static synchronized String[] userLists(long user_id){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(USER_LIST);
            stmt.registerOutParameter(1, Types.ARRAY);
            stmt.setString(2, String.valueOf(user_id));
            stmt.execute();
            ResultSet res = stmt.getArray(1).getResultSet();
            connection.close();

            ArrayList<String> tmp = new ArrayList<>(0);
            while (res.next())
                tmp.add(res.getString(0));
            return (String[])tmp.toArray();
        } catch (SQLException throwables) {
            BotLogger.severe("LIST", throwables.getMessage(), throwables);
            return new String[0];
        }
    }

    public static synchronized boolean createList(String user_name, String name){
        try{
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(INSERT_LIST);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setString(2, user_name);
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
