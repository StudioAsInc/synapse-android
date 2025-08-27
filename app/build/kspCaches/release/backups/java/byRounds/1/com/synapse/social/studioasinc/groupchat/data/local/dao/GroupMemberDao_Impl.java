package com.synapse.social.studioasinc.groupchat.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.synapse.social.studioasinc.groupchat.data.model.GroupMember;
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
public final class GroupMemberDao_Impl implements GroupMemberDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GroupMember> __insertionAdapterOfGroupMember;

  private final EntityDeletionOrUpdateAdapter<GroupMember> __updateAdapterOfGroupMember;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMemberRole;

  private final SharedSQLiteStatement __preparedStmtOfRemoveMember;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastSeen;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGroupMember;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllGroupMembers;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMembers;

  public GroupMemberDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroupMember = new EntityInsertionAdapter<GroupMember>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `group_members` (`id`,`groupId`,`userId`,`userName`,`userPhotoUrl`,`role`,`joinedAt`,`addedBy`,`isActive`,`lastSeenAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupMember entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getUserId());
        statement.bindString(4, entity.getUserName());
        statement.bindString(5, entity.getUserPhotoUrl());
        statement.bindString(6, entity.getRole());
        statement.bindLong(7, entity.getJoinedAt());
        statement.bindString(8, entity.getAddedBy());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getLastSeenAt());
      }
    };
    this.__updateAdapterOfGroupMember = new EntityDeletionOrUpdateAdapter<GroupMember>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `group_members` SET `id` = ?,`groupId` = ?,`userId` = ?,`userName` = ?,`userPhotoUrl` = ?,`role` = ?,`joinedAt` = ?,`addedBy` = ?,`isActive` = ?,`lastSeenAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupMember entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getUserId());
        statement.bindString(4, entity.getUserName());
        statement.bindString(5, entity.getUserPhotoUrl());
        statement.bindString(6, entity.getRole());
        statement.bindLong(7, entity.getJoinedAt());
        statement.bindString(8, entity.getAddedBy());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getLastSeenAt());
        statement.bindString(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateMemberRole = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_members SET role = ? WHERE groupId = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveMember = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_members SET isActive = 0 WHERE groupId = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastSeen = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_members SET lastSeenAt = ? WHERE groupId = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteGroupMember = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_members WHERE groupId = ? AND userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllGroupMembers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_members WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllMembers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_members";
        return _query;
      }
    };
  }

  @Override
  public Object insertGroupMember(final GroupMember member,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupMember.insert(member);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertGroupMembers(final List<GroupMember> members,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupMember.insert(members);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGroupMember(final GroupMember member,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGroupMember.handle(member);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMemberRole(final String groupId, final String userId, final String role,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMemberRole.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, role);
        _argIndex = 2;
        _stmt.bindString(_argIndex, groupId);
        _argIndex = 3;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfUpdateMemberRole.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeMember(final String groupId, final String userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveMember.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, groupId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfRemoveMember.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastSeen(final String groupId, final String userId, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastSeen.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, groupId);
        _argIndex = 3;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfUpdateLastSeen.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGroupMember(final String groupId, final String userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGroupMember.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, groupId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, userId);
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
          __preparedStmtOfDeleteGroupMember.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllGroupMembers(final String groupId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllGroupMembers.acquire();
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
          __preparedStmtOfDeleteAllGroupMembers.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllMembers(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMembers.acquire();
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
          __preparedStmtOfDeleteAllMembers.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GroupMember>> getGroupMembersFlow(final String groupId) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND isActive = 1 ORDER BY role DESC, joinedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_members"}, new Callable<List<GroupMember>>() {
      @Override
      @NonNull
      public List<GroupMember> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final List<GroupMember> _result = new ArrayList<GroupMember>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMember _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _item = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Object getGroupMembers(final String groupId,
      final Continuation<? super List<GroupMember>> $completion) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND isActive = 1 ORDER BY role DESC, joinedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMember>>() {
      @Override
      @NonNull
      public List<GroupMember> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final List<GroupMember> _result = new ArrayList<GroupMember>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMember _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _item = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Object getGroupMember(final String groupId, final String userId,
      final Continuation<? super GroupMember> $completion) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GroupMember>() {
      @Override
      @Nullable
      public GroupMember call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final GroupMember _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _result = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Flow<GroupMember> getGroupMemberFlow(final String groupId, final String userId) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_members"}, new Callable<GroupMember>() {
      @Override
      @Nullable
      public GroupMember call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final GroupMember _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _result = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Object getGroupAdmins(final String groupId,
      final Continuation<? super List<GroupMember>> $completion) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND role IN ('OWNER', 'ADMIN') AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMember>>() {
      @Override
      @NonNull
      public List<GroupMember> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final List<GroupMember> _result = new ArrayList<GroupMember>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMember _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _item = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Object searchGroupMembers(final String groupId, final String query,
      final Continuation<? super List<GroupMember>> $completion) {
    final String _sql = "SELECT * FROM group_members WHERE groupId = ? AND userName LIKE '%' || ? || '%' AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMember>>() {
      @Override
      @NonNull
      public List<GroupMember> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfUserPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "userPhotoUrl");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfJoinedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "joinedAt");
          final int _cursorIndexOfAddedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "addedBy");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfLastSeenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenAt");
          final List<GroupMember> _result = new ArrayList<GroupMember>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMember _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpUserPhotoUrl;
            _tmpUserPhotoUrl = _cursor.getString(_cursorIndexOfUserPhotoUrl);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final long _tmpJoinedAt;
            _tmpJoinedAt = _cursor.getLong(_cursorIndexOfJoinedAt);
            final String _tmpAddedBy;
            _tmpAddedBy = _cursor.getString(_cursorIndexOfAddedBy);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpLastSeenAt;
            _tmpLastSeenAt = _cursor.getLong(_cursorIndexOfLastSeenAt);
            _item = new GroupMember(_tmpId,_tmpGroupId,_tmpUserId,_tmpUserName,_tmpUserPhotoUrl,_tmpRole,_tmpJoinedAt,_tmpAddedBy,_tmpIsActive,_tmpLastSeenAt);
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
  public Object getGroupMemberCount(final String groupId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM group_members WHERE groupId = ? AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
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
