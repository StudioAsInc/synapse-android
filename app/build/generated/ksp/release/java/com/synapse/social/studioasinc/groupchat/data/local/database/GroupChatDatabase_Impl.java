package com.synapse.social.studioasinc.groupchat.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao_Impl;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao_Impl;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao_Impl;
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao;
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GroupChatDatabase_Impl extends GroupChatDatabase {
  private volatile GroupDao _groupDao;

  private volatile GroupMemberDao _groupMemberDao;

  private volatile GroupMessageDao _groupMessageDao;

  private volatile UserDao _userDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `groups` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `iconUrl` TEXT NOT NULL, `createdBy` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `memberCount` INTEGER NOT NULL, `isPrivate` INTEGER NOT NULL, `maxMembers` INTEGER NOT NULL, `lastMessageId` TEXT NOT NULL, `lastMessageText` TEXT NOT NULL, `lastMessageTime` INTEGER NOT NULL, `lastMessageSender` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `settings` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `group_members` (`id` TEXT NOT NULL, `groupId` TEXT NOT NULL, `userId` TEXT NOT NULL, `userName` TEXT NOT NULL, `userPhotoUrl` TEXT NOT NULL, `role` TEXT NOT NULL, `joinedAt` INTEGER NOT NULL, `addedBy` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `lastSeenAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `group_messages` (`id` TEXT NOT NULL, `groupId` TEXT NOT NULL, `senderId` TEXT NOT NULL, `senderName` TEXT NOT NULL, `senderPhotoUrl` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `messageType` TEXT NOT NULL, `attachments` TEXT NOT NULL, `replyToMessageId` TEXT NOT NULL, `isEdited` INTEGER NOT NULL, `editedAt` INTEGER NOT NULL, `isDeleted` INTEGER NOT NULL, `deletedAt` INTEGER NOT NULL, `seenBy` TEXT NOT NULL, `reactions` TEXT NOT NULL, `isSystemMessage` INTEGER NOT NULL, `systemMessageType` TEXT NOT NULL, `deliveryStatus` TEXT NOT NULL, `localId` TEXT NOT NULL, `priority` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `username` TEXT NOT NULL, `displayName` TEXT NOT NULL, `email` TEXT NOT NULL, `photoUrl` TEXT NOT NULL, `bio` TEXT NOT NULL, `isOnline` INTEGER NOT NULL, `lastSeen` INTEGER NOT NULL, `fcmToken` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `phoneNumber` TEXT NOT NULL, `isVerified` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8ebc917ff775566d36ddc65535c1020c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `groups`");
        db.execSQL("DROP TABLE IF EXISTS `group_members`");
        db.execSQL("DROP TABLE IF EXISTS `group_messages`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsGroups = new HashMap<String, TableInfo.Column>(16);
        _columnsGroups.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("iconUrl", new TableInfo.Column("iconUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("createdBy", new TableInfo.Column("createdBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("memberCount", new TableInfo.Column("memberCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("isPrivate", new TableInfo.Column("isPrivate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("maxMembers", new TableInfo.Column("maxMembers", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("lastMessageId", new TableInfo.Column("lastMessageId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("lastMessageText", new TableInfo.Column("lastMessageText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("lastMessageTime", new TableInfo.Column("lastMessageTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("lastMessageSender", new TableInfo.Column("lastMessageSender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroups.put("settings", new TableInfo.Column("settings", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGroups = new TableInfo("groups", _columnsGroups, _foreignKeysGroups, _indicesGroups);
        final TableInfo _existingGroups = TableInfo.read(db, "groups");
        if (!_infoGroups.equals(_existingGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "groups(com.synapse.social.studioasinc.groupchat.data.model.Group).\n"
                  + " Expected:\n" + _infoGroups + "\n"
                  + " Found:\n" + _existingGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsGroupMembers = new HashMap<String, TableInfo.Column>(10);
        _columnsGroupMembers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("userName", new TableInfo.Column("userName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("userPhotoUrl", new TableInfo.Column("userPhotoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("joinedAt", new TableInfo.Column("joinedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("addedBy", new TableInfo.Column("addedBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("lastSeenAt", new TableInfo.Column("lastSeenAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGroupMembers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGroupMembers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGroupMembers = new TableInfo("group_members", _columnsGroupMembers, _foreignKeysGroupMembers, _indicesGroupMembers);
        final TableInfo _existingGroupMembers = TableInfo.read(db, "group_members");
        if (!_infoGroupMembers.equals(_existingGroupMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "group_members(com.synapse.social.studioasinc.groupchat.data.model.GroupMember).\n"
                  + " Expected:\n" + _infoGroupMembers + "\n"
                  + " Found:\n" + _existingGroupMembers);
        }
        final HashMap<String, TableInfo.Column> _columnsGroupMessages = new HashMap<String, TableInfo.Column>(21);
        _columnsGroupMessages.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("senderId", new TableInfo.Column("senderId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("senderName", new TableInfo.Column("senderName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("senderPhotoUrl", new TableInfo.Column("senderPhotoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("messageType", new TableInfo.Column("messageType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("attachments", new TableInfo.Column("attachments", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("replyToMessageId", new TableInfo.Column("replyToMessageId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("isEdited", new TableInfo.Column("isEdited", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("editedAt", new TableInfo.Column("editedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("isDeleted", new TableInfo.Column("isDeleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("deletedAt", new TableInfo.Column("deletedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("seenBy", new TableInfo.Column("seenBy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("reactions", new TableInfo.Column("reactions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("isSystemMessage", new TableInfo.Column("isSystemMessage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("systemMessageType", new TableInfo.Column("systemMessageType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("deliveryStatus", new TableInfo.Column("deliveryStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("localId", new TableInfo.Column("localId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMessages.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGroupMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGroupMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGroupMessages = new TableInfo("group_messages", _columnsGroupMessages, _foreignKeysGroupMessages, _indicesGroupMessages);
        final TableInfo _existingGroupMessages = TableInfo.read(db, "group_messages");
        if (!_infoGroupMessages.equals(_existingGroupMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "group_messages(com.synapse.social.studioasinc.groupchat.data.model.GroupMessage).\n"
                  + " Expected:\n" + _infoGroupMessages + "\n"
                  + " Found:\n" + _existingGroupMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(14);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("photoUrl", new TableInfo.Column("photoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("bio", new TableInfo.Column("bio", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isOnline", new TableInfo.Column("isOnline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("lastSeen", new TableInfo.Column("lastSeen", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fcmToken", new TableInfo.Column("fcmToken", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isVerified", new TableInfo.Column("isVerified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.synapse.social.studioasinc.groupchat.data.model.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8ebc917ff775566d36ddc65535c1020c", "79b54d5d45e430e0371f9274fdc5c16e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "groups","group_members","group_messages","users");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `groups`");
      _db.execSQL("DELETE FROM `group_members`");
      _db.execSQL("DELETE FROM `group_messages`");
      _db.execSQL("DELETE FROM `users`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(GroupDao.class, GroupDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GroupMemberDao.class, GroupMemberDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GroupMessageDao.class, GroupMessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public GroupDao groupDao() {
    if (_groupDao != null) {
      return _groupDao;
    } else {
      synchronized(this) {
        if(_groupDao == null) {
          _groupDao = new GroupDao_Impl(this);
        }
        return _groupDao;
      }
    }
  }

  @Override
  public GroupMemberDao groupMemberDao() {
    if (_groupMemberDao != null) {
      return _groupMemberDao;
    } else {
      synchronized(this) {
        if(_groupMemberDao == null) {
          _groupMemberDao = new GroupMemberDao_Impl(this);
        }
        return _groupMemberDao;
      }
    }
  }

  @Override
  public GroupMessageDao groupMessageDao() {
    if (_groupMessageDao != null) {
      return _groupMessageDao;
    } else {
      synchronized(this) {
        if(_groupMessageDao == null) {
          _groupMessageDao = new GroupMessageDao_Impl(this);
        }
        return _groupMessageDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
