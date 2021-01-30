package organizer.data.base.connection.utils;

import org.telegram.telegrambots.meta.logging.BotLogger;
import organizer.bot.utils.menus.MainMenu;
import organizer.data.base.connection.Connection;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class User {

    private static final String ADD_USER = "{? = call data.add_user(?, ?, ?)}";

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
            BotLogger.severe("USER", throwables.getMessage(), throwables);
            return -1;
        }
    }

}
