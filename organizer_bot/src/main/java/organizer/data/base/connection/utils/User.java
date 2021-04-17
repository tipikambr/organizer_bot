package organizer.data.base.connection.utils;

import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.bot.utils.menus.MainMenu;
import organizer.data.base.connection.Connection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class User {

    private static final String ADD_USER = "{? = call data.add_user(?, ?, ?)}";
    private static final String GET_USER_NAME = "{? = call data.get_owner_name(?)}";
    private static final String NOTIFICATION = "{? = call data.is_notification_on(?)}";
    private static final String IS_OWNER = "{? = call data.is_owner(?, ?)}";
    private static final String SOUND_ON_OFF = "{? = call data.change_notification(?, ?)}";

    public static boolean setSound(long userId, Boolean sound) {
        try {
            Connection connection = new Connection();
            CallableStatement stmt = connection.getConnection().prepareCall(SOUND_ON_OFF);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, userId);
            stmt.setBoolean(3, sound);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            return res;


        } catch (SQLException throwables) {
            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static boolean isSoundOn(long userId) {
        try {
            Connection connection = new Connection();
            CallableStatement stmt = connection.getConnection().prepareCall(NOTIFICATION);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, userId);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            return res;


        } catch (SQLException throwables) {
            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static boolean isOwner(long userId, String owner) {
        try {
            Connection connection = new Connection();
            CallableStatement stmt = connection.getConnection().prepareCall(IS_OWNER);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setLong(2, userId);
            stmt.setString(3, owner);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            return res;


        } catch (SQLException throwables) {
            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return false;
        }
    }

    public static String getUserName(long id) {
        try {
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(GET_USER_NAME);
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setLong(2, id);

            stmt.execute();
            String res = stmt.getString(1);
            connection.close();
            return res;


        } catch (SQLException throwables) {
            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return null;
        }
    }

    /**
     * Добавляет пользователя в базу данных
     * @param id telegram_id чата, который надо добавить в базу данных
     * @return
     *      1   -   Новый пользователь, или повторно авторизованный
     *      0   -   Старый пользователь
     *      -1  -   Произошла ошибка
     */
    public static int authUser(long id, String name, String password){
        try {
            Connection connection = new Connection();

            CallableStatement stmt = connection.getConnection().prepareCall(ADD_USER);
            stmt.registerOutParameter(1, Types.BOOLEAN);
            stmt.setString(2, String.valueOf(name));
            stmt.setInt(3, password.hashCode());
            stmt.setLong(4, id);

            stmt.execute();
            boolean res = stmt.getBoolean(1);
            connection.close();
            if (res)
                return 1;
            return 0;


        } catch (SQLException throwables) {
//            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return -1;
        }
    }

}
