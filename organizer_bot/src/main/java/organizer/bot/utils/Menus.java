package organizer.bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import organizer.bot.utils.menus.*;
import organizer.bot.utils.menus.list.ListMenu;
import organizer.bot.utils.menus.mail.MailMenu;

public class Menus {

    ///////////////////////////////////////////////////////////////
    //                       MENU CONSTANT                       //
    ///////////////////////////////////////////////////////////////

    public final static int WRITING_NAME = -1;
    public final static int WRITING_PASSWORD = -2;
    public final static int MAIN_MENU = 0;

    public final static int MAIL_MENU = 1;
        public final static int ADD_MAIL = 11;
            public final static int WRITE_MAIL = 111;
            public final static int WRITE_MAIL_PASSWORD = 112;
            public final static int CONFIRM = 113;
            public final static int CANCEL = 114;
        public final static int CHECK_MAIL = 12;
            //IMPORTANT Здесь подразумевается, что будут под каждой почтой кнопки "Детальнее" и "Удалить"
                //IMPORTANT Из "Удалить" будет две кнопочки на подтвержение
            public final static int ADD_MAIL_CHECKER = 121;
                public final static int SELECT_MAIL_CHECKER_TYPE = 1211;
                public final static int WRITING_MAIL_CHECKER_PATTERN = 1212;
                public final static int SAVE_CHECKER = 1213;
                public final static int CANCEL_CHECKER = 1214;
            public final static int LOOK_MAIL_CHECKERS = 122;
                //IMPORTANT Здесь подразумевается, что будут под каждым чекером будут кнопки "Изменить" и "Удалить"
                public final static int MODIFY_MAIL_CHECKER = 1221;
                public final static int DELETE_MAIL_CHECKER = 1222;
                    //IMPORTANT Кнопочки "Подтвердить" и "Отменить"
            public final static int SOUND_EDITING_MAIL = 129;
        public final static int SOUND_EDITING_ALL_MAILS = 19;

    public final static int LIST_MENU = 2;
        public final static int WRITING_LIST_NAME = -21;
            //IMPORTANT Еще будет кнопка "Сохранить"
        public final static int CHECK_AVAILABLE_LIST = 22;
            //IMPORTANT Под каждым названием списка будет кнопка "Посмотреть"
            public final static int ADD_LIST_ITEM = 221;
                public final static int WRITING_NAME_ITEM = 2211;
                public final static int WRITING_DETAILS_OF_ITEM = 2212;
                public final static int CANCEL_ADDING_ITEM = 2213;
                //IMPORTANT Так же будет кнопочка "Добавить".
            public final static int DELETE_LIST_ITEM = 222;
                //IMPORTANT Кнопочки "Подтвердить" и "Отменить"
            public final static int MODIFY_LIST_ITEM = 223;
                public final static int WRITING_NEW_NAME_ITEM = 2231;
                public final static int WRITING_NEW_DETAILS_OF_ITEM = 2232;
                public final static int CANCEL_CHANGING_ITEM = 2233;
                //IMPORTANT Еще будет кнопка "Сохранить"
        public final static int SOUND_EDITING_LIST = 29;

    public final static int LANGUAGE_MENU = 3;
    public final static int SOUND_MENU = 9;

    ///////////////////////////////////////////////////////////////
    //                      MENU FUNCTIONS                       //
    ///////////////////////////////////////////////////////////////

    //FIXME
    public static boolean isEditing(Integer state){
//        return state != null && state >= 10;
        return state != null && state < 0;
    }

    public static int getUpperMenuNumber(Integer state){
        return state / 10;
    }

    public static ReplyKeyboardMarkup getMenu(Integer state, Integer language){
        if (state == null) return MainMenu.getMainMenu(language);
        return switch (state){
            case 1 -> MailMenu.getMailMenu(language);
            case 2 -> ListMenu.getListMenu(language);
            case 3 -> LanguageMenu.getLanguageMenu(language);
            case 9 -> SoundMenu.getSoundMenu(language);
            default -> MainMenu.getMainMenu(language);
        };
    }
}
