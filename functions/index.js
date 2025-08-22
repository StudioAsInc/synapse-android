const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendChatNotification = functions.database.ref("/skyline/chats/{uid1}/{uid2}/{messageId}")
    .onCreate(async (snapshot, context) => {
        const messageId = context.params.messageId;
        const uid1 = context.params.uid1;
        const uid2 = context.params.uid2;
        const messageData = snapshot.val();

        // Prevent function from running on message deletion
        if (!messageData) {
            return functions.logger.log("Message data is null, exiting.");
        }

        const senderId = messageData.uid;
        const recipientId = senderId === uid1 ? uid2 : uid1;

        // Check if a notification has already been sent for this message
        const sentRef = admin.database().ref(`/sent_notifications/${messageId}`);
        const sentSnapshot = await sentRef.once("value");
        if (sentSnapshot.exists()) {
            return functions.logger.log("Notification already sent for message:", messageId);
        }

        // Get recipient's user data
        const userRef = admin.database().ref(`/skyline/users/${recipientId}`);
        const userSnapshot = await userRef.once("value");
        const userData = userSnapshot.val();

        if (!userData) {
            return functions.logger.log("Recipient user data not found:", recipientId);
        }

        // Check if recipient is offline
        if (userData.status === "online") {
            return functions.logger.log("Recipient is online, no notification needed:", recipientId);
        }

        const fcmToken = userData.fcmToken;
        if (!fcmToken) {
            return functions.logger.log("Recipient does not have an FCM token:", recipientId);
        }

        // Get sender's name for the notification
        const senderUserRef = admin.database().ref(`/skyline/users/${senderId}`);
        const senderUserSnapshot = await senderUserRef.once("value");
        const senderUserData = senderUserSnapshot.val();
        const senderName = senderUserData.nickname !== "null" ? senderUserData.nickname : senderUserData.username;

        // Notification payload
        const payload = {
            data: {
                title: `New message from ${senderName}`,
                body: messageData.message_text,
                senderUid: senderId,
            },
        };

        try {
            // Send notification
            await admin.messaging().sendToDevice(fcmToken, payload);
            functions.logger.log("Notification sent successfully to:", recipientId);

            // Mark notification as sent
            await sentRef.set(true);

        } catch (error) {
            functions.logger.error("Error sending notification:", error);
        }

        return null;
    });
