package me.virco.tasktimer;

import java.io.Serializable;

/**
 * Created by marco on 6/4/2017.
 *
 */

class Task implements Serializable {
    public static final long serialVersionUID = 20170604L;

    private long m_Id;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    Task(long id, String name, String description, int sortOrder) {
        this.m_Id = id;
        mName = name;
        mDescription = description;
        mSortOrder = sortOrder;
    }

    long get_Id() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    String getDescription() {
        return mDescription;
    }

    int getSortOrder() {
        return mSortOrder;
    }

    public void set_Id(long id) {
        this.m_Id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "m_Id=" + m_Id +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }
}
