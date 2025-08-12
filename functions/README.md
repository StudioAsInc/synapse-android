# Synapse Chat Notifications Cloud Function

This Cloud Function sends a push notification using OneSignal when a new message is sent in the Synapse chat application.

## Prerequisites

Before you can deploy this function, you need to have the following installed:

*   [Node.js](https://nodejs.org/) (version 18 or higher is recommended)
*   [Firebase CLI](https://firebase.google.com/docs/cli)

You also need to have a Firebase project set up and be logged into the Firebase CLI.

## Setup and Deployment

1.  **Navigate to the functions directory:**

    ```bash
    cd functions
    ```

2.  **Install dependencies:**

    ```bash
    npm install
    ```

3.  **Configure environment variables:**

    This function requires two environment variables to be set for OneSignal integration:

    *   `onesignal.app_id`: Your OneSignal App ID.
    *   `onesignal.app_auth_key`: Your OneSignal REST API Key.

    You can set these variables using the Firebase CLI with the following commands. Replace the placeholder values with your actual OneSignal credentials.

    ```bash
    firebase functions:config:set onesignal.app_id="YOUR_ONESIGNAL_APP_ID"
    firebase functions:config:set onesignal.app_auth_key="YOUR_ONESIGNAL_REST_API_KEY"
    ```

4.  **Deploy the function:**

    Once the environment variables are set, you can deploy the function to your Firebase project:

    ```bash
    firebase deploy --only functions
    ```

After deployment, the `sendNewMessageNotification` function will be active and will automatically send a push notification whenever a new message is added to the `/skyline/chats/{receiverId}/{senderId}/{messageId}` path in your Firebase Realtime Database.
