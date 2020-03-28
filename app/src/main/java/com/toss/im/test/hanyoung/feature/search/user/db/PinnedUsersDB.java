package com.toss.im.test.hanyoung.feature.search.user.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lib_api.model.BaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class PinnedUsersDB {
    private final String PINNED_USER_DB_NAME = "pinned_user";
    private final String PINNED_USER_TABLE_NAME = "pinned_user_table";
    private final String PINNED_USER_TABLE_COLUMN_ID = "_id";

    private SQLiteDatabase pinnedUserDB;

    public PinnedUsersDB(@NonNull Activity activity) {
        try {
            pinnedUserDB = activity.openOrCreateDatabase(PINNED_USER_DB_NAME, Activity.MODE_PRIVATE, null);
            pinnedUserDB.execSQL("CREATE TABLE IF NOT EXISTS " + PINNED_USER_TABLE_NAME + " (_id VARCHAR(100) NOT NULL UNIQUE );");
        } catch (NullPointerException e) {
            Log.e("SearchUserFragment", "failed load database", e);
        }
    }

    /**
     * 원래는 keyword로 조회해야 좋지만 서버 검색정책과 sql 검색정책이 다를수 있으니 전체row 가져온다.
     */
    public List<String> getPinnedUserList (String keyword) {
        List<String> pinnedUserList = new ArrayList<>();

        Cursor cursor = pinnedUserDB.rawQuery("select * from " + PINNED_USER_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                pinnedUserList.add(cursor.getString(cursor.getColumnIndex(PINNED_USER_TABLE_COLUMN_ID)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return pinnedUserList;
    }

    public void addUser (@NonNull BaseUser baseUser) {
        String id = baseUser.getId();

        ContentValues initialValues = new ContentValues();
        initialValues.put(PINNED_USER_TABLE_COLUMN_ID, id);

        pinnedUserDB.insertWithOnConflict(PINNED_USER_TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void deleteUser (@NonNull BaseUser baseUser) {
        String id = baseUser.getId();

        pinnedUserDB.delete(PINNED_USER_TABLE_NAME, PINNED_USER_TABLE_COLUMN_ID + "=?", new String[]{id});
    }

    public void close () {
        pinnedUserDB.close();
    }
}
