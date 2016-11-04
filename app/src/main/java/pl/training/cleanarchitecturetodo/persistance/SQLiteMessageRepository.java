package pl.training.cleanarchitecturetodo.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import pl.training.cleanarchitecturetodo.domain.Message;
import pl.training.cleanarchitecturetodo.domain.MessageRepository;

public class SQLiteMessageRepository extends SQLiteOpenHelper implements MessageRepository {

    private static final String COL_ID = "_id";
    private static final String COL_CONTENT = "content";
    private static final String COL_SUBMISSION_DATE = "submissionDate";
    private static final String COL_CURRENT_STATE = "currentState";
    private static final String COL_SEND_DATE = "sendDate";
    private static final String TABLE_MESSAGE = "message";
    private static final String[] COL_ALL = {COL_ID, COL_CONTENT, COL_SUBMISSION_DATE, COL_CURRENT_STATE, COL_SEND_DATE};

    @Inject
    public SQLiteMessageRepository(Context context) {
        super(context, "messages", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table message(" +
                        "_id INTEGER PRIMARY KEY, " +
                        "content TEXT NOT NULL, " +
                        "submissionDate LONG NOT NULL, " +
                        "currentState TEXT NOT NULL, " +
                        "sendDate LONG" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void save(Message message) {
        ContentValues cv = createContentValues(message);
        if (message.getId() == null) {
            long messageId = getWritableDatabase().insert(TABLE_MESSAGE, null, cv);
            message.setId(messageId);
        } else {
            getWritableDatabase().update(TABLE_MESSAGE, cv, "_id = ?", new String[]{message.getId().toString()});
        }
    }

    @Override
    public Message load(Long id) {
        if (id == null) return null;
        Cursor cursor = getReadableDatabase().query(TABLE_MESSAGE, COL_ALL, "_id = ?", new String[]{id.toString()}, null, null, null);
        try {
            if (!cursor.moveToFirst()) {
                return null;
            }
            return loadFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    @Override
    public List<Message> loadPending() {
        Cursor cursor = getReadableDatabase()
                .query(TABLE_MESSAGE, COL_ALL, "currentState = ?", new String[]{Message.State.PENDING.toString()}, null, null, null, null);

        try {
            List<Message> messages = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                messages.add(loadFromCursor(cursor));
            }
            return messages;
        } finally {
            cursor.close();
        }
    }

    @NonNull
    private Message loadFromCursor(Cursor cursor) {
        long messageId = cursor.getLong(cursor.getColumnIndex(COL_ID));
        String content = cursor.getString(cursor.getColumnIndex(COL_CONTENT));
        Date submissionDate = loadDate(cursor.getLong(cursor.getColumnIndex(COL_SUBMISSION_DATE)));
        Message.State currentState = Message.State.valueOf(cursor.getString(cursor.getColumnIndex(COL_CURRENT_STATE)));

        int sendDateColIdx = cursor.getColumnIndex(COL_SEND_DATE);
        Date sendDate = loadDate(cursor.isNull(sendDateColIdx) ? null : cursor.getLong(sendDateColIdx));

        return new Message(messageId, content, submissionDate, currentState, sendDate);
    }

    @NonNull
    private ContentValues createContentValues(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTENT, message.getContent());
        contentValues.put(COL_SUBMISSION_DATE, storeDate(message.getSubmissionDate()));
        contentValues.put(COL_CURRENT_STATE, message.getState().toString());
        contentValues.put(COL_SEND_DATE, storeDate(message.getSendTime()));
        return contentValues;
    }

    @Nullable
    private Date loadDate(Long time) {
        return time != null ? new Date(time) : null;
    }

    @Nullable
    private Long storeDate(Date date) {
        return date != null ? date.getTime() : null;
    }
}
