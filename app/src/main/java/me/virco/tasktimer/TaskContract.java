package me.virco.tasktimer;

import android.provider.BaseColumns;

/**
 * Created by mvbrenes on 5/24/17.
 */

public class TaskContract {

    static final String TABLE_NAME = "Tasks";

    // Tasks fields
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORT_ORDER = "SortOrder";

        private Columns() {
            //  private constructor to prevent initialization
        }
    }
}
