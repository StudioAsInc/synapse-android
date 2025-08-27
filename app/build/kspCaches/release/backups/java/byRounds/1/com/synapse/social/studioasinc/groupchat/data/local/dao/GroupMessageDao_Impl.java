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
import com.synapse.social.studioasinc.groupchat.data.model.GroupMessage;
import com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GroupMessageDao_Impl implements GroupMessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GroupMessage> __insertionAdapterOfGroupMessage;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<GroupMessage> __updateAdapterOfGroupMessage;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDeliveryStatus;

  private final SharedSQLiteStatement __preparedStmtOfEditMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessage;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSeenBy;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReactions;

  private final SharedSQLiteStatement __preparedStmtOfPermanentlyDeleteMessage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllGroupMessages;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllMessages;

  public GroupMessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroupMessage = new EntityInsertionAdapter<GroupMessage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `group_messages` (`id`,`groupId`,`senderId`,`senderName`,`senderPhotoUrl`,`text`,`timestamp`,`messageType`,`attachments`,`replyToMessageId`,`isEdited`,`editedAt`,`isDeleted`,`deletedAt`,`seenBy`,`reactions`,`isSystemMessage`,`systemMessageType`,`deliveryStatus`,`localId`,`priority`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupMessage entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getSenderId());
        statement.bindString(4, entity.getSenderName());
        statement.bindString(5, entity.getSenderPhotoUrl());
        statement.bindString(6, entity.getText());
        statement.bindLong(7, entity.getTimestamp());
        statement.bindString(8, entity.getMessageType());
        final String _tmp = __converters.fromAttachmentList(entity.getAttachments());
        statement.bindString(9, _tmp);
        statement.bindString(10, entity.getReplyToMessageId());
        final int _tmp_1 = entity.isEdited() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        statement.bindLong(12, entity.getEditedAt());
        final int _tmp_2 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(13, _tmp_2);
        statement.bindLong(14, entity.getDeletedAt());
        final String _tmp_3 = __converters.fromStringLongMap(entity.getSeenBy());
        statement.bindString(15, _tmp_3);
        final String _tmp_4 = __converters.fromStringStringListMap(entity.getReactions());
        statement.bindString(16, _tmp_4);
        final int _tmp_5 = entity.isSystemMessage() ? 1 : 0;
        statement.bindLong(17, _tmp_5);
        statement.bindString(18, entity.getSystemMessageType());
        statement.bindString(19, entity.getDeliveryStatus());
        statement.bindString(20, entity.getLocalId());
        statement.bindString(21, entity.getPriority());
      }
    };
    this.__updateAdapterOfGroupMessage = new EntityDeletionOrUpdateAdapter<GroupMessage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `group_messages` SET `id` = ?,`groupId` = ?,`senderId` = ?,`senderName` = ?,`senderPhotoUrl` = ?,`text` = ?,`timestamp` = ?,`messageType` = ?,`attachments` = ?,`replyToMessageId` = ?,`isEdited` = ?,`editedAt` = ?,`isDeleted` = ?,`deletedAt` = ?,`seenBy` = ?,`reactions` = ?,`isSystemMessage` = ?,`systemMessageType` = ?,`deliveryStatus` = ?,`localId` = ?,`priority` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupMessage entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getSenderId());
        statement.bindString(4, entity.getSenderName());
        statement.bindString(5, entity.getSenderPhotoUrl());
        statement.bindString(6, entity.getText());
        statement.bindLong(7, entity.getTimestamp());
        statement.bindString(8, entity.getMessageType());
        final String _tmp = __converters.fromAttachmentList(entity.getAttachments());
        statement.bindString(9, _tmp);
        statement.bindString(10, entity.getReplyToMessageId());
        final int _tmp_1 = entity.isEdited() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        statement.bindLong(12, entity.getEditedAt());
        final int _tmp_2 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(13, _tmp_2);
        statement.bindLong(14, entity.getDeletedAt());
        final String _tmp_3 = __converters.fromStringLongMap(entity.getSeenBy());
        statement.bindString(15, _tmp_3);
        final String _tmp_4 = __converters.fromStringStringListMap(entity.getReactions());
        statement.bindString(16, _tmp_4);
        final int _tmp_5 = entity.isSystemMessage() ? 1 : 0;
        statement.bindLong(17, _tmp_5);
        statement.bindString(18, entity.getSystemMessageType());
        statement.bindString(19, entity.getDeliveryStatus());
        statement.bindString(20, entity.getLocalId());
        statement.bindString(21, entity.getPriority());
        statement.bindString(22, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDeliveryStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_messages SET deliveryStatus = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfEditMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_messages SET text = ?, isEdited = 1, editedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_messages SET isDeleted = 1, deletedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSeenBy = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_messages SET seenBy = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReactions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_messages SET reactions = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfPermanentlyDeleteMessage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_messages WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllGroupMessages = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_messages WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllMessages = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_messages";
        return _query;
      }
    };
  }

  @Override
  public Object insertMessage(final GroupMessage message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupMessage.insert(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMessages(final List<GroupMessage> messages,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupMessage.insert(messages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMessage(final GroupMessage message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGroupMessage.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDeliveryStatus(final String messageId, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDeliveryStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfUpdateDeliveryStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object editMessage(final String messageId, final String newText, final long editedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfEditMessage.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, newText);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, editedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfEditMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessage(final String messageId, final long deletedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, deletedAt);
        _argIndex = 2;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfDeleteMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSeenBy(final String messageId, final Map<String, Long> seenBy,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSeenBy.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromStringLongMap(seenBy);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfUpdateSeenBy.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReactions(final String messageId,
      final Map<String, ? extends List<String>> reactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReactions.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromStringStringListMap(reactions);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfUpdateReactions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object permanentlyDeleteMessage(final String messageId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfPermanentlyDeleteMessage.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, messageId);
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
          __preparedStmtOfPermanentlyDeleteMessage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllGroupMessages(final String groupId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllGroupMessages.acquire();
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
          __preparedStmtOfDeleteAllGroupMessages.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllMessages(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllMessages.acquire();
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
          __preparedStmtOfDeleteAllMessages.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public PagingSource<Integer, GroupMessage> getGroupMessagesPaged(final String groupId) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND isDeleted = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    return new LimitOffsetPagingSource<GroupMessage>(_statement, __db, "group_messages") {
      @Override
      @NonNull
      protected List<GroupMessage> convertRows(@NonNull final Cursor cursor) {
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(cursor, "id");
        final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(cursor, "groupId");
        final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(cursor, "senderId");
        final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(cursor, "senderName");
        final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(cursor, "senderPhotoUrl");
        final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(cursor, "text");
        final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(cursor, "timestamp");
        final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(cursor, "messageType");
        final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(cursor, "attachments");
        final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(cursor, "replyToMessageId");
        final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(cursor, "isEdited");
        final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(cursor, "editedAt");
        final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(cursor, "isDeleted");
        final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(cursor, "deletedAt");
        final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(cursor, "seenBy");
        final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(cursor, "reactions");
        final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(cursor, "isSystemMessage");
        final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(cursor, "systemMessageType");
        final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(cursor, "deliveryStatus");
        final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(cursor, "localId");
        final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(cursor, "priority");
        final List<GroupMessage> _result = new ArrayList<GroupMessage>(cursor.getCount());
        while (cursor.moveToNext()) {
          final GroupMessage _item;
          final String _tmpId;
          _tmpId = cursor.getString(_cursorIndexOfId);
          final String _tmpGroupId;
          _tmpGroupId = cursor.getString(_cursorIndexOfGroupId);
          final String _tmpSenderId;
          _tmpSenderId = cursor.getString(_cursorIndexOfSenderId);
          final String _tmpSenderName;
          _tmpSenderName = cursor.getString(_cursorIndexOfSenderName);
          final String _tmpSenderPhotoUrl;
          _tmpSenderPhotoUrl = cursor.getString(_cursorIndexOfSenderPhotoUrl);
          final String _tmpText;
          _tmpText = cursor.getString(_cursorIndexOfText);
          final long _tmpTimestamp;
          _tmpTimestamp = cursor.getLong(_cursorIndexOfTimestamp);
          final String _tmpMessageType;
          _tmpMessageType = cursor.getString(_cursorIndexOfMessageType);
          final List<MessageAttachment> _tmpAttachments;
          final String _tmp;
          _tmp = cursor.getString(_cursorIndexOfAttachments);
          _tmpAttachments = __converters.toAttachmentList(_tmp);
          final String _tmpReplyToMessageId;
          _tmpReplyToMessageId = cursor.getString(_cursorIndexOfReplyToMessageId);
          final boolean _tmpIsEdited;
          final int _tmp_1;
          _tmp_1 = cursor.getInt(_cursorIndexOfIsEdited);
          _tmpIsEdited = _tmp_1 != 0;
          final long _tmpEditedAt;
          _tmpEditedAt = cursor.getLong(_cursorIndexOfEditedAt);
          final boolean _tmpIsDeleted;
          final int _tmp_2;
          _tmp_2 = cursor.getInt(_cursorIndexOfIsDeleted);
          _tmpIsDeleted = _tmp_2 != 0;
          final long _tmpDeletedAt;
          _tmpDeletedAt = cursor.getLong(_cursorIndexOfDeletedAt);
          final Map<String, Long> _tmpSeenBy;
          final String _tmp_3;
          _tmp_3 = cursor.getString(_cursorIndexOfSeenBy);
          _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
          final Map<String, List<String>> _tmpReactions;
          final String _tmp_4;
          _tmp_4 = cursor.getString(_cursorIndexOfReactions);
          _tmpReactions = __converters.toStringStringListMap(_tmp_4);
          final boolean _tmpIsSystemMessage;
          final int _tmp_5;
          _tmp_5 = cursor.getInt(_cursorIndexOfIsSystemMessage);
          _tmpIsSystemMessage = _tmp_5 != 0;
          final String _tmpSystemMessageType;
          _tmpSystemMessageType = cursor.getString(_cursorIndexOfSystemMessageType);
          final String _tmpDeliveryStatus;
          _tmpDeliveryStatus = cursor.getString(_cursorIndexOfDeliveryStatus);
          final String _tmpLocalId;
          _tmpLocalId = cursor.getString(_cursorIndexOfLocalId);
          final String _tmpPriority;
          _tmpPriority = cursor.getString(_cursorIndexOfPriority);
          _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
          _result.add(_item);
        }
        return _result;
      }
    };
  }

  @Override
  public Object getRecentMessages(final String groupId, final int limit,
      final Continuation<? super List<GroupMessage>> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND isDeleted = 0 ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMessage>>() {
      @Override
      @NonNull
      public List<GroupMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<GroupMessage> _result = new ArrayList<GroupMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Flow<List<GroupMessage>> getRecentMessagesFlow(final String groupId, final int limit) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND isDeleted = 0 ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_messages"}, new Callable<List<GroupMessage>>() {
      @Override
      @NonNull
      public List<GroupMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<GroupMessage> _result = new ArrayList<GroupMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object getMessageById(final String messageId,
      final Continuation<? super GroupMessage> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GroupMessage>() {
      @Override
      @Nullable
      public GroupMessage call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final GroupMessage _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _result = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Flow<GroupMessage> getMessageByIdFlow(final String messageId) {
    final String _sql = "SELECT * FROM group_messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_messages"}, new Callable<GroupMessage>() {
      @Override
      @Nullable
      public GroupMessage call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final GroupMessage _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _result = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object getMessageByLocalId(final String localId,
      final Continuation<? super GroupMessage> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE localId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, localId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GroupMessage>() {
      @Override
      @Nullable
      public GroupMessage call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final GroupMessage _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _result = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object searchMessages(final String groupId, final String query,
      final Continuation<? super List<GroupMessage>> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND text LIKE '%' || ? || '%' AND isDeleted = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMessage>>() {
      @Override
      @NonNull
      public List<GroupMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<GroupMessage> _result = new ArrayList<GroupMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object getPendingMessages(final String groupId,
      final Continuation<? super List<GroupMessage>> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND deliveryStatus = 'SENDING' OR deliveryStatus = 'FAILED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMessage>>() {
      @Override
      @NonNull
      public List<GroupMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<GroupMessage> _result = new ArrayList<GroupMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object getMessagesByUser(final String groupId, final String userId,
      final Continuation<? super List<GroupMessage>> $completion) {
    final String _sql = "SELECT * FROM group_messages WHERE groupId = ? AND senderId = ? AND isDeleted = 0 ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupMessage>>() {
      @Override
      @NonNull
      public List<GroupMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfSenderName = CursorUtil.getColumnIndexOrThrow(_cursor, "senderName");
          final int _cursorIndexOfSenderPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhotoUrl");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfAttachments = CursorUtil.getColumnIndexOrThrow(_cursor, "attachments");
          final int _cursorIndexOfReplyToMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "replyToMessageId");
          final int _cursorIndexOfIsEdited = CursorUtil.getColumnIndexOrThrow(_cursor, "isEdited");
          final int _cursorIndexOfEditedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "editedAt");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfDeletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deletedAt");
          final int _cursorIndexOfSeenBy = CursorUtil.getColumnIndexOrThrow(_cursor, "seenBy");
          final int _cursorIndexOfReactions = CursorUtil.getColumnIndexOrThrow(_cursor, "reactions");
          final int _cursorIndexOfIsSystemMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemMessage");
          final int _cursorIndexOfSystemMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "systemMessageType");
          final int _cursorIndexOfDeliveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryStatus");
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final List<GroupMessage> _result = new ArrayList<GroupMessage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMessage _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpSenderId;
            _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            final String _tmpSenderName;
            _tmpSenderName = _cursor.getString(_cursorIndexOfSenderName);
            final String _tmpSenderPhotoUrl;
            _tmpSenderPhotoUrl = _cursor.getString(_cursorIndexOfSenderPhotoUrl);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpMessageType;
            _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            final List<MessageAttachment> _tmpAttachments;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfAttachments);
            _tmpAttachments = __converters.toAttachmentList(_tmp);
            final String _tmpReplyToMessageId;
            _tmpReplyToMessageId = _cursor.getString(_cursorIndexOfReplyToMessageId);
            final boolean _tmpIsEdited;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsEdited);
            _tmpIsEdited = _tmp_1 != 0;
            final long _tmpEditedAt;
            _tmpEditedAt = _cursor.getLong(_cursorIndexOfEditedAt);
            final boolean _tmpIsDeleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_2 != 0;
            final long _tmpDeletedAt;
            _tmpDeletedAt = _cursor.getLong(_cursorIndexOfDeletedAt);
            final Map<String, Long> _tmpSeenBy;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfSeenBy);
            _tmpSeenBy = __converters.toStringLongMap(_tmp_3);
            final Map<String, List<String>> _tmpReactions;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfReactions);
            _tmpReactions = __converters.toStringStringListMap(_tmp_4);
            final boolean _tmpIsSystemMessage;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSystemMessage);
            _tmpIsSystemMessage = _tmp_5 != 0;
            final String _tmpSystemMessageType;
            _tmpSystemMessageType = _cursor.getString(_cursorIndexOfSystemMessageType);
            final String _tmpDeliveryStatus;
            _tmpDeliveryStatus = _cursor.getString(_cursorIndexOfDeliveryStatus);
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpPriority;
            _tmpPriority = _cursor.getString(_cursorIndexOfPriority);
            _item = new GroupMessage(_tmpId,_tmpGroupId,_tmpSenderId,_tmpSenderName,_tmpSenderPhotoUrl,_tmpText,_tmpTimestamp,_tmpMessageType,_tmpAttachments,_tmpReplyToMessageId,_tmpIsEdited,_tmpEditedAt,_tmpIsDeleted,_tmpDeletedAt,_tmpSeenBy,_tmpReactions,_tmpIsSystemMessage,_tmpSystemMessageType,_tmpDeliveryStatus,_tmpLocalId,_tmpPriority);
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
  public Object getMessageCount(final String groupId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM group_messages WHERE groupId = ? AND isDeleted = 0";
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

  @Override
  public Object getUnreadMessageCount(final String groupId, final String currentUserId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM group_messages WHERE groupId = ? AND senderId != ? AND NOT EXISTS (SELECT 1 FROM json_each(seenBy) WHERE json_each.key = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    _argIndex = 2;
    _statement.bindString(_argIndex, currentUserId);
    _argIndex = 3;
    _statement.bindString(_argIndex, currentUserId);
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
