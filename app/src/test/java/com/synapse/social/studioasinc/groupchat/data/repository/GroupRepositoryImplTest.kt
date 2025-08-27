package com.synapse.social.studioasinc.groupchat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao
import com.synapse.social.studioasinc.groupchat.data.model.*
import com.synapse.social.studioasinc.groupchat.data.repository.impl.GroupRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class GroupRepositoryImplTest {

    @Mock
    private lateinit var groupDao: GroupDao

    @Mock
    private lateinit var groupMemberDao: GroupMemberDao

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseDatabase: FirebaseDatabase

    @Mock
    private lateinit var firebaseStorage: FirebaseStorage

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var repository: GroupRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = GroupRepositoryImpl(
            groupDao,
            groupMemberDao,
            userDao,
            firebaseAuth,
            firebaseDatabase,
            firebaseStorage
        )
    }

    @Test
    fun `getGroupById returns success when group exists in local database`() = runTest {
        // Given
        val groupId = "test-group-id"
        val expectedGroup = Group(
            id = groupId,
            name = "Test Group",
            description = "Test Description",
            createdBy = "user123",
            memberCount = 5
        )
        
        whenever(groupDao.getGroupById(groupId)).thenReturn(expectedGroup)

        // When
        val result = repository.getGroupById(groupId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedGroup, result.getOrNull())
        verify(groupDao).getGroupById(groupId)
    }

    @Test
    fun `getGroupById returns failure when group not found`() = runTest {
        // Given
        val groupId = "non-existent-group"
        whenever(groupDao.getGroupById(groupId)).thenReturn(null)

        // When
        val result = repository.getGroupById(groupId)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `addMember returns success when user has permissions`() = runTest {
        // Given
        val groupId = "test-group-id"
        val userId = "user123"
        val newUser = User(
            id = "new-user",
            username = "newuser",
            displayName = "New User",
            email = "new@example.com"
        )
        val role = UserRole.MEMBER.name
        
        val adderMember = GroupMember(
            id = "member123",
            groupId = groupId,
            userId = userId,
            role = UserRole.ADMIN.name
        )
        
        val group = Group(
            id = groupId,
            name = "Test Group",
            memberCount = 5,
            maxMembers = 256
        )

        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn(userId)
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(adderMember)
        whenever(groupMemberDao.getGroupMember(groupId, newUser.id)).thenReturn(null)
        whenever(groupDao.getGroupById(groupId)).thenReturn(group)

        // When
        val result = repository.addMember(groupId, newUser, role, userId)

        // Then
        assertTrue(result.isSuccess)
        verify(groupMemberDao).insertGroupMember(any())
        verify(groupDao).updateMemberCount(groupId, 6)
    }

    @Test
    fun `addMember returns failure when user lacks permissions`() = runTest {
        // Given
        val groupId = "test-group-id"
        val userId = "user123"
        val newUser = User(
            id = "new-user",
            username = "newuser",
            displayName = "New User",
            email = "new@example.com"
        )
        val role = UserRole.MEMBER.name
        
        val adderMember = GroupMember(
            id = "member123",
            groupId = groupId,
            userId = userId,
            role = UserRole.MEMBER.name // No permission to add members
        )

        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn(userId)
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(adderMember)

        // When
        val result = repository.addMember(groupId, newUser, role, userId)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Insufficient permissions") == true)
    }

    @Test
    fun `removeMember returns success when user can remove member`() = runTest {
        // Given
        val groupId = "test-group-id"
        val currentUserId = "admin-user"
        val targetUserId = "target-user"
        
        val adminMember = GroupMember(
            id = "admin-member",
            groupId = groupId,
            userId = currentUserId,
            role = UserRole.ADMIN.name
        )
        
        val targetMember = GroupMember(
            id = "target-member",
            groupId = groupId,
            userId = targetUserId,
            role = UserRole.MEMBER.name
        )
        
        val group = Group(
            id = groupId,
            name = "Test Group",
            memberCount = 5
        )

        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn(currentUserId)
        whenever(groupMemberDao.getGroupMember(groupId, currentUserId)).thenReturn(adminMember)
        whenever(groupMemberDao.getGroupMember(groupId, targetUserId)).thenReturn(targetMember)
        whenever(groupDao.getGroupById(groupId)).thenReturn(group)

        // When
        val result = repository.removeMember(groupId, targetUserId)

        // Then
        assertTrue(result.isSuccess)
        verify(groupMemberDao).removeMember(groupId, targetUserId)
        verify(groupDao).updateMemberCount(groupId, 4)
    }

    @Test
    fun `updateMemberRole returns success when user has promotion permissions`() = runTest {
        // Given
        val groupId = "test-group-id"
        val currentUserId = "admin-user"
        val targetUserId = "target-user"
        val newRole = UserRole.MANAGER.name
        
        val adminMember = GroupMember(
            id = "admin-member",
            groupId = groupId,
            userId = currentUserId,
            role = UserRole.ADMIN.name
        )
        
        val targetMember = GroupMember(
            id = "target-member",
            groupId = groupId,
            userId = targetUserId,
            role = UserRole.MEMBER.name
        )

        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn(currentUserId)
        whenever(groupMemberDao.getGroupMember(groupId, currentUserId)).thenReturn(adminMember)
        whenever(groupMemberDao.getGroupMember(groupId, targetUserId)).thenReturn(targetMember)

        // When
        val result = repository.updateMemberRole(groupId, targetUserId, newRole)

        // Then
        assertTrue(result.isSuccess)
        verify(groupMemberDao).updateMemberRole(groupId, targetUserId, newRole)
    }

    @Test
    fun `canUserPerformAction returns correct permissions for different roles`() = runTest {
        // Given
        val groupId = "test-group-id"
        val userId = "user123"
        
        // Test ADMIN permissions
        val adminMember = GroupMember(
            id = "admin-member",
            groupId = groupId,
            userId = userId,
            role = UserRole.ADMIN.name
        )
        
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(adminMember)

        // When & Then
        val manageResult = repository.canUserPerformAction(groupId, userId, "MANAGE_MEMBERS")
        assertTrue(manageResult.isSuccess)
        assertTrue(manageResult.getOrNull() == true)
        
        val deleteResult = repository.canUserPerformAction(groupId, userId, "DELETE_MESSAGES")
        assertTrue(deleteResult.isSuccess)
        assertTrue(deleteResult.getOrNull() == true)
        
        // Test MEMBER permissions
        val memberMember = GroupMember(
            id = "member-member",
            groupId = groupId,
            userId = userId,
            role = UserRole.MEMBER.name
        )
        
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(memberMember)
        
        val memberManageResult = repository.canUserPerformAction(groupId, userId, "MANAGE_MEMBERS")
        assertTrue(memberManageResult.isSuccess)
        assertFalse(memberManageResult.getOrNull() == true)
    }

    @Test
    fun `getGroupMemberCount returns correct count`() = runTest {
        // Given
        val groupId = "test-group-id"
        val expectedCount = 10
        
        whenever(groupMemberDao.getGroupMemberCount(groupId)).thenReturn(expectedCount)

        // When
        val result = repository.getGroupMemberCount(groupId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedCount, result.getOrNull())
    }

    @Test
    fun `isUserMember returns true when user is active member`() = runTest {
        // Given
        val groupId = "test-group-id"
        val userId = "user123"
        val member = GroupMember(
            id = "member123",
            groupId = groupId,
            userId = userId,
            isActive = true
        )
        
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(member)

        // When
        val result = repository.isUserMember(groupId, userId)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() == true)
    }

    @Test
    fun `isUserMember returns false when user is not member`() = runTest {
        // Given
        val groupId = "test-group-id"
        val userId = "user123"
        
        whenever(groupMemberDao.getGroupMember(groupId, userId)).thenReturn(null)

        // When
        val result = repository.isUserMember(groupId, userId)

        // Then
        assertTrue(result.isSuccess)
        assertFalse(result.getOrNull() == true)
    }
}