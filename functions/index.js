const functions = require("firebase-functions");
const admin = require("firebase-admin");
const OneSignal = require("onesignal-node");

admin.initializeApp();

const oneSignalClient = new OneSignal.Client({
    userAuthKey: functions.config().onesignal.user_auth_key,
    app: { appAuthKey: functions.config().onesignal.app_auth_key, appId: functions.config().onesignal.app_id }
});

exports.sendNewMessageNotification = functions.database.ref('/skyline/chats/{receiverId}/{senderId}/{messageId}')
    .onCreate(async (snapshot, context) => {
        const message = snapshot.val();
        const receiverId = context.params.receiverId;
        const senderId = context.params.senderId;

        // Get the sender's name
        const senderSnapshot = await admin.database().ref(`/skyline/users/${senderId}`).once('value');
        const sender = senderSnapshot.val();
        const senderName = sender.nickname || `@${sender.username}`;

        const notification = {
            contents: {
                'en': message.message_text,
            },
            headings: {
                'en': senderName,
            },
            include_external_user_ids: [receiverId],
        };

        try {
            const response = await oneSignalClient.createNotification(notification);
            console.log('OneSignal notification sent successfully:', response.body);
        } catch (e) {
            console.error('Error sending OneSignal notification:', e);
        }
    });
