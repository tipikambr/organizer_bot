package organizer.bot.utils.constants;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import organizer.bot.utils.menus.*;
import organizer.bot.utils.menus.alarm.AlarmMenu;
import organizer.bot.utils.menus.language.LanguageMenu;
import organizer.bot.utils.menus.list.ListMenu;
import organizer.bot.utils.menus.mail.MailMenu;
import organizer.bot.utils.menus.sound.SoundMenu;

public class Menus {

    ///////////////////////////////////////////////////////////////
    //                       MENU CONSTANT                       //
    ///////////////////////////////////////////////////////////////

    public final static int WRITING_NAME = -1;
    public final static int WRITING_PASSWORD = -2;
    public final static int MAIN_MENU = 0;

    public final static int MAIL_MENU = 1;
        public final static int ADD_MAIL = -11;
        public final static int ADD_MAIL_PASSWORD = -12;
        public final static int CHECK_MAIL = -13;
            public final static int CHECK_MAIL_FILTERS = 14;
                public final static int ADD_MAIL_CHECKER = -141;

    public final static int LIST_MENU = 2;
        public final static int WRITING_LIST_NAME = -21;
        public final static int CHECK_AVAILABLE_LIST = -22;
            public final static int LIST_DETAILS_MENU = 23;
            public final static int ADD_LIST_ITEM = -231;
            public final static int SHARE_LIST = -232;
            public final static int LOOK_USERS_SHARED = -233;

    public final static int ALARM_MENU = 3;
        public final static int ALARM_ADD = 31;
            public final static int ALARM_ADD_HOUR = 311;
                public final static int ALARM_ADD_MINUTE = 3111;
        public final static int ALARM_LOOK = 32;


    public final static int TASK_MENU= 4;
        public final static int ADD_TASK_NAME = -41;
                public final static int ADD_TASK_TIME = 42;
                    public final static int ADD_TASK_HOUR = 43;
                        public final static int ADD_TASK_MINUTE = 44;
                            public final static int ADD_TASK_MINUTE_WAIT = -45;
        public final static int LOOK_TASK = 45;



    public final static int LANGUAGE_MENU = 8;
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
        return Math.abs(state) / 10;
    }

    public static ReplyKeyboardMarkup getMenu(Integer state, Integer language){
        if (state == null) return MainMenu.getMainMenu(language);
        return switch (state){
            case 1 -> MailMenu.getMailMenu(language);
            case 2 -> ListMenu.getListMenu(language);
            case 3 -> AlarmMenu.getAlarmMenu(language);
            case 8 -> LanguageMenu.getLanguageMenu(language);
            case 9 -> SoundMenu.getSoundMenu(language);
            default -> MainMenu.getMainMenu(language);
        };
    }
}
