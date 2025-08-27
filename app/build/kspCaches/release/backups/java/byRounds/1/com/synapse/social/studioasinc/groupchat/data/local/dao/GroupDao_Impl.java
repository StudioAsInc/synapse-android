package com.synapse.social.studioasinc.groupchat.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.paging.LimitOffsetPagingSource;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.synapse.social.studioasinc.groupchat.data.local.database.Converters;
import com.synapse.social.studioasinc.groupchat.data.model.Group;
import com.synapse.social.studioasinc.groupchat.data.model.GroupSettings;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GroupDao_Impl implements GroupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Group> __insertionAdapterOfGroup;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Group> __updateAdapterOfGroup;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastMessage;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMemberCount;

  private final SharedSQLiteStatement __preparedStmtOfMarkGroupAsInactive;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGroup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllGroups;

  public GroupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroup = new EntityInsertionAdapter<Group>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `groups` (`id`,`name`,`description`,`iconUrl`,`createdBy`,`createdAt`,`updatedAt`,`memberCount`,`isPrivate`,`maxMembers`,`lastMessageId`,`lastMessageText`,`lastMessageTime`,`lastMessageSender`,`isActive`,`settings`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Group entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getIconUrl());
        statement.bindString(5, entity.getCreatedBy());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindLong(8, entity.getMemberCount());
        final int _tmp = entity.isPrivate() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getMaxMembers());
        statement.bindString(11, entity.getLastMessageId());
        statement.bindString(12, entity.getLastMessageText());
        statement.bindLong(13, entity.getLastMessageTime());
        statement.bindString(14, entity.getLastMessageSender());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(15, _tmp_1);
        final String _tmp_2 = __converters.fromGroupSettings(entity.getSettings());
        statement.bindString(16, _tmp_2);
      }
    };
    this.__updateAdapterOfGroup = new EntityDeletionOrUpdateAdapter<Group>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `groups` SET `id` = ?,`name` = ?,`description` = ?,`iconUrl` = ?,`createdBy` = ?,`createdAt` = ?,`updatedAt` = ?,`memberCount` = ?,`isPrivate` = ?,`maxMembers` = ?,`lastMessageId` = ?,`lastMessageText` = ?,`lastMessageTime` = ?,`lastMessageSender` = ?,`isActive` = ?,`settings` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Group entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        statement.bindString(4, entity.getIconUrl());
        statement.bindString(5, entity.getCreatedBy());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindLong(8, entity.getMemberCount());
        final int _tmp = entity.isPrivate() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getMaxMembers());
        statement.bindString(11, entity.getLastMessageId());
        statement.bindString(12, entity.getLastMessageText());
        statement.bindLong(13, entity.getLastMessageTime());
        statement.bindString(14, entity.getLastMessageSender());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(15, _tmp_1);
        final String _tmp_2 = __converters.fromGroupSettings(entity.getSettings());
        statement.bindString(16, _tmp_2);
        statement.bindString(17, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateLastMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE groups SET lastMessageId = ?, lastMessageText = ?, lastMessageTime = ?, lastMessageSender = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMemberCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE groups SET memberCount = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkGroupAsInactive = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE groups SET isActive = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM groups WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllGroups = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM groups";
        return _query;
      }
    };
  }

  @Override
  public Object insertGroup(final Group group, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroup.insert(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertGroups(final List<Group> groups,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroup.insert(groups);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGroup(final Group group, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGroup.handle(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastMessage(final String groupId, final String messageId,
      final String messageText, final long messageTime, final String sender,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastMessage.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, messageId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, messageText);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, messageTime);
        _argIndex = 4;
        _stmt.bindString(_argIndex, sender);
        _argIndex = 5;
        _stmt.bindString(_argIndex, groupId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateLastMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMemberCount(final String groupId, final int count,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMemberCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindString(_argIndex, groupId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateMemberCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markGroupAsInactive(final String groupId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkGroupAsInactive.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, groupId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkGroupAsInactive.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGroup(final String groupId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGroup.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, groupId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteGroup.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllGroups(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllGroups.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllGroups.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public PagingSource<Integer, Group> getAllGroupsPaged() {
    final String _sql = "SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new LimitOffsetPagingSource<Group>(_statement, __db, "groups") {
      @Override
      @NonNull
      protected List<Group> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(cursor, "name");
        final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(cursor, "description");
        final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(cursor, "iconUrl");
        final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(cursor, "createdBy");
        final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "createdAt");
        final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(cursor, "updatedAt");
        final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(cursor, "memberCount");
        final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(cursor, "isPrivate");
        final int _cursorIndexOfMaxMembers = CursorUtil.getColumnIndexOrThrow(cursor, "maxMembers");
        final int _cursorIndexOfLastMessageId = CursorUtil.getColumnIndexOrThrow(cursor, "lastMessageId");
        final int _cursorIndexOfLastMessageText = CursorUtil.getColumnIndexOrThrow(cursor, "lastMessageText");
        final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(cursor, "lastMessageTime");
        final int _cursorIndexOfLastMessageSender = CursorUtil.getColumnIndexOrThrow(cursor, "lastMessageSender");
        final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(cursor, "isActive");
        final int _cursorIndexOfSettings = CursorUtil.getColumnIndexOrThrow(cursor, "settings");
        final List<Group> _result = new ArrayList<Group>(cursor.getCount());
        while (cursor.moveToNext()) {
          final Group _item;
          final String _tmpId;
          _tmpId = cursor.getString(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = cursor.getString(_cursorIndexOfName);
          final String _tmpDescription;
          _tmpDescription = cursor.getString(_cursorIndexOfDescription);
          final String _tmpIconUrl;
          _tmpIconUrl = cursor.getString(_cursorIndexOfIconUrl);
          final String _tmpCreatedBy;
          _tmpCreatedBy = cursor.getString(_cursorIndexOfCreatedBy);
          final long _tmpCreatedAt;
          _tmpCreatedAt = cursor.getLong(_cursorIndexOfCreatedAt);
          final long _tmpUpdatedAt;
          _tmpUpdatedAt = cursor.getLong(_cursorIndexOfUpdatedAt);
          final int _tmpMemberCount;
          _tmpMemberCount = cursor.getInt(_cursorIndexOfMemberCount);
          final boolean _tmpIsPrivate;
          final int _tmp;
          _tmp = cursor.getInt(_cursorIndexOfIsPrivate);
          _tmpIsPrivate = _tmp != 0;
          final int _tmpMaxMembers;
          _tmpMaxMembers = cursor.getInt(_cursorIndexOfMaxMembers);
          final String _tmpLastMessageId;
          _tmpLastMessageId = cursor.getString(_cursorIndexOfLastMessageId);
          final String _tmpLastMessageText;
          _tmpLastMessageText = cursor.getString(_cursorIndexOfLastMessageText);
          final long _tmpLastMessageTime;
          _tmpLastMessageTime = cursor.getLong(_cursorIndexOfLastMessageTime);
          final String _tmpLastMessageSender;
          _tmpLastMessageSender = cursor.getString(_cursorIndexOfLastMessageSender);
          final boolean _tmpIsActive;
          final int _tmp_1;
          _tmp_1 = cursor.getInt(_cursorIndexOfIsActive);
          _tmpIsActive = _tmp_1 != 0;
          final GroupSettings _tmpSettings;
          final String _tmp_2;
          _tmp_2 = cursor.getString(_cursorIndexOfSettings);
          _tmpSettings = __converters.toGroupSettings(_tmp_2);
          _item = new Group(_tmpId,_tmpName,_tmpDescription,_tmpIconUrl,_tmpCreatedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpMemberCount,_tmpIsPrivate,_tmpMaxMembers,_tmpLastMessageId,_tmpLastMessageText,_tmpLastMessageTime,_tmpLastMessageSender,_tmpIsActive,_tmpSettings);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Flow<List<Group>> getAllGroupsFlow() {
    final String _sql = "SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"groups"}, new Callable<List<Group>>() {
      @Override
      @NonNull
      public List<Group> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfMaxMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "maxMembers");
          final int _cursorIndexOfLastMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageId");
          final int _cursorIndexOfLastMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageText");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfLastMessageSender = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageSender");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "settings");
          final List<Group> _result = new ArrayList<Group>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Group _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpIconUrl;
            _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final int _tmpMaxMembers;
            _tmpMaxMembers = _cursor.getInt(_cursorIndexOfMaxMembers);
            final String _tmpLastMessageId;
            _tmpLastMessageId = _cursor.getString(_cursorIndexOfLastMessageId);
            final String _tmpLastMessageText;
            _tmpLastMessageText = _cursor.getString(_cursorIndexOfLastMessageText);
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final String _tmpLastMessageSender;
            _tmpLastMessageSender = _cursor.getString(_cursorIndexOfLastMessageSender);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final GroupSettings _tmpSettings;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSettings);
            _tmpSettings = __converters.toGroupSettings(_tmp_2);
            _item = new Group(_tmpId,_tmpName,_tmpDescription,_tmpIconUrl,_tmpCreatedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpMemberCount,_tmpIsPrivate,_tmpMaxMembers,_tmpLastMessageId,_tmpLastMessageText,_tmpLastMessageTime,_tmpLastMessageSender,_tmpIsActive,_tmpSettings);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getGroupById(final String groupId, final Continuation<? super Group> $completion) {
    final String _sql = "SELECT * FROM groups WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Group>() {
      @Override
      @Nullable
      public Group call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfMaxMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "maxMembers");
          final int _cursorIndexOfLastMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageId");
          final int _cursorIndexOfLastMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageText");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfLastMessageSender = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageSender");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "settings");
          final Group _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpIconUrl;
            _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final int _tmpMaxMembers;
            _tmpMaxMembers = _cursor.getInt(_cursorIndexOfMaxMembers);
            final String _tmpLastMessageId;
            _tmpLastMessageId = _cursor.getString(_cursorIndexOfLastMessageId);
            final String _tmpLastMessageText;
            _tmpLastMessageText = _cursor.getString(_cursorIndexOfLastMessageText);
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final String _tmpLastMessageSender;
            _tmpLastMessageSender = _cursor.getString(_cursorIndexOfLastMessageSender);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final GroupSettings _tmpSettings;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSettings);
            _tmpSettings = __converters.toGroupSettings(_tmp_2);
            _result = new Group(_tmpId,_tmpName,_tmpDescription,_tmpIconUrl,_tmpCreatedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpMemberCount,_tmpIsPrivate,_tmpMaxMembers,_tmpLastMessageId,_tmpLastMessageText,_tmpLastMessageTime,_tmpLastMessageSender,_tmpIsActive,_tmpSettings);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Group> getGroupByIdFlow(final String groupId) {
    final String _sql = "SELECT * FROM groups WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"groups"}, new Callable<Group>() {
      @Override
      @Nullable
      public Group call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfMaxMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "maxMembers");
          final int _cursorIndexOfLastMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageId");
          final int _cursorIndexOfLastMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageText");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfLastMessageSender = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageSender");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "settings");
          final Group _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpIconUrl;
            _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final int _tmpMaxMembers;
            _tmpMaxMembers = _cursor.getInt(_cursorIndexOfMaxMembers);
            final String _tmpLastMessageId;
            _tmpLastMessageId = _cursor.getString(_cursorIndexOfLastMessageId);
            final String _tmpLastMessageText;
            _tmpLastMessageText = _cursor.getString(_cursorIndexOfLastMessageText);
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final String _tmpLastMessageSender;
            _tmpLastMessageSender = _cursor.getString(_cursorIndexOfLastMessageSender);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final GroupSettings _tmpSettings;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSettings);
            _tmpSettings = __converters.toGroupSettings(_tmp_2);
            _result = new Group(_tmpId,_tmpName,_tmpDescription,_tmpIconUrl,_tmpCreatedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpMemberCount,_tmpIsPrivate,_tmpMaxMembers,_tmpLastMessageId,_tmpLastMessageText,_tmpLastMessageTime,_tmpLastMessageSender,_tmpIsActive,_tmpSettings);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object searchGroups(final String query,
      final Continuation<? super List<Group>> $completion) {
    final String _sql = "SELECT * FROM groups WHERE name LIKE '%' || ? || '%' AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Group>>() {
      @Override
      @NonNull
      public List<Group> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
          final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfMaxMembers = CursorUtil.getColumnIndexOrThrow(_cursor, "maxMembers");
          final int _cursorIndexOfLastMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageId");
          final int _cursorIndexOfLastMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageText");
          final int _cursorIndexOfLastMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageTime");
          final int _cursorIndexOfLastMessageSender = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessageSender");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfSettings = CursorUtil.getColumnIndexOrThrow(_cursor, "settings");
          final List<Group> _result = new ArrayList<Group>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Group _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpIconUrl;
            _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
            final String _tmpCreatedBy;
            _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final int _tmpMaxMembers;
            _tmpMaxMembers = _cursor.getInt(_cursorIndexOfMaxMembers);
            final String _tmpLastMessageId;
            _tmpLastMessageId = _cursor.getString(_cursorIndexOfLastMessageId);
            final String _tmpLastMessageText;
            _tmpLastMessageText = _cursor.getString(_cursorIndexOfLastMessageText);
            final long _tmpLastMessageTime;
            _tmpLastMessageTime = _cursor.getLong(_cursorIndexOfLastMessageTime);
            final String _tmpLastMessageSender;
            _tmpLastMessageSender = _cursor.getString(_cursorIndexOfLastMessageSender);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final GroupSettings _tmpSettings;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfSettings);
            _tmpSettings = __converters.toGroupSettings(_tmp_2);
            _item = new Group(_tmpId,_tmpName,_tmpDescription,_tmpIconUrl,_tmpCreatedBy,_tmpCreatedAt,_tmpUpdatedAt,_tmpMemberCount,_tmpIsPrivate,_tmpMaxMembers,_tmpLastMessageId,_tmpLastMessageText,_tmpLastMessageTime,_tmpLastMessageSender,_tmpIsActive,_tmpSettings);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveGroupCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM groups WHERE isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
