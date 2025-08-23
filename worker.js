// Enhanced Cloudflare Worker for detailed push notifications via OneSignal
// This worker receives notification requests and forwards them to OneSignal's REST API
// Now includes sender information, message type, and image previews

export default {
  async fetch(request, env, ctx) {
    // Handle CORS preflight requests
    if (request.method === 'OPTIONS') {
      return new Response(null, {
        status: 200,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization',
          'Access-Control-Max-Age': '86400',
        },
      });
    }

    // Only allow POST requests
    if (request.method !== 'POST') {
      return new Response('Method not allowed', { status: 405 });
    }

    try {
      // Parse the request body
      const requestData = await request.json();
      const { 
        recipientUserId, 
        notificationMessage, 
        messageType = 'text',
        attachments = [],
        senderName = 'Someone',
        timestamp
      } = requestData;

      // Validate required fields
      if (!recipientUserId || !notificationMessage) {
        return new Response(
          JSON.stringify({ 
            error: 'Missing required fields: recipientUserId and notificationMessage' 
          }), 
          { 
            status: 400,
            headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }
        );
      }

      // OneSignal configuration
      const ONESIGNAL_APP_ID = '044e1911-6911-4871-95f9-d60003002fe2';
      const ONESIGNAL_REST_API_KEY = env.ONESIGNAL_REST_API_KEY;

      if (!ONESIGNAL_REST_API_KEY) {
        console.error('OneSignal REST API key not configured');
        return new Response(
          JSON.stringify({ error: 'Server configuration error' }), 
          { 
            status: 500,
            headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }
        );
      }

      // Create enhanced notification content based on message type
      let notificationTitle, notificationContent, notificationImage;
      
      if (messageType === 'attachment' && attachments && attachments.length > 0) {
        // Handle attachment messages with smart detection
        const firstAttachment = attachments[0];
        const isImage = firstAttachment.match(/\.(jpg|jpeg|png|gif|webp)$/i);
        const isVideo = firstAttachment.match(/\.(mp4|avi|mov|mkv|webm)$/i);
        const isAudio = firstAttachment.match(/\.(mp3|wav|aac|ogg)$/i);
        
        if (attachments.length === 1) {
          if (isImage) {
            notificationTitle = `${senderName} sent you a photo`;
            notificationContent = '📷 Tap to view';
            notificationImage = firstAttachment;
          } else if (isVideo) {
            notificationTitle = `${senderName} sent you a video`;
            notificationContent = '🎥 Tap to view';
            notificationImage = firstAttachment; // Video thumbnail if available
          } else if (isAudio) {
            notificationTitle = `${senderName} sent you a voice message`;
            notificationContent = '🎵 Tap to listen';
          } else {
            notificationTitle = `${senderName} sent you a file`;
            notificationContent = '📎 Tap to download';
          }
        } else {
          // Multiple attachments
          if (isImage) {
            notificationTitle = `${senderName} sent you ${attachments.length} photos`;
            notificationContent = `📷 ${attachments.length} photos • Tap to view`;
            notificationImage = firstAttachment;
          } else if (isVideo) {
            notificationTitle = `${senderName} sent you ${attachments.length} videos`;
            notificationContent = `🎥 ${attachments.length} videos • Tap to view`;
            notificationImage = firstAttachment;
          } else {
            notificationTitle = `${senderName} sent you ${attachments.length} files`;
            notificationContent = `📎 ${attachments.length} files • Tap to view`;
          }
        }
      } else if (messageType === 'text') {
        // Handle text messages
        notificationTitle = `${senderName}`;
        notificationContent = notificationMessage;
      } else if (messageType === 'reply') {
        // Handle reply messages
        notificationTitle = `${senderName} replied to a message`;
        notificationContent = notificationMessage;
      } else {
        // Fallback for other message types
        notificationTitle = `${senderName} sent you a message`;
        notificationContent = notificationMessage;
      }

      // Prepare the enhanced notification payload for OneSignal
      const notificationPayload = {
        app_id: ONESIGNAL_APP_ID,
        include_player_ids: [recipientUserId],
        contents: {
          en: notificationContent
        },
        headings: {
          en: notificationTitle
        },
        // Add image preview for attachment messages
        ...(notificationImage && { big_picture: notificationImage }),
        // Enhanced data payload
        data: {
          message: notificationMessage,
          messageType: messageType,
          senderName: senderName,
          attachments: attachments,
          timestamp: timestamp || new Date().toISOString(),
          // Additional metadata for rich notifications
          isAttachment: messageType === 'attachment',
          attachmentCount: attachments ? attachments.length : 0,
          hasImage: attachments && attachments.length > 0
        },
        // Enhanced notification settings
        priority: 10, // High priority for messages
        android_accent_color: '#FF6B6B', // Custom accent color
        android_led_color: '#FF6B6B', // LED color for Android
        android_sound: 'default', // Default notification sound
        // iOS specific settings
        ios_sound: 'default',
        // Rich notification settings
        chrome_web_image: notificationImage,
        firefox_web_image: notificationImage,
        // Action buttons for rich interactions
        web_buttons: [
          {
            id: "view_message",
            text: "View Message",
            url: "https://your-app.com/chat" // Replace with your app's deep link
          }
        ]
      };

      // Send notification to OneSignal
      const oneSignalResponse = await fetch('https://onesignal.com/api/v1/notifications', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Basic ${ONESIGNAL_REST_API_KEY}`,
        },
        body: JSON.stringify(notificationPayload),
      });

      if (!oneSignalResponse.ok) {
        const errorText = await oneSignalResponse.text();
        console.error('OneSignal API error:', errorText);
        return new Response(
          JSON.stringify({ 
            error: 'Failed to send notification via OneSignal',
            details: errorText
          }), 
          { 
            status: oneSignalResponse.status,
            headers: {
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }
        );
      }

      const oneSignalResult = await oneSignalResponse.json();
      
      // Log successful enhanced notification
      console.log('Enhanced notification sent successfully:', {
        recipientId: recipientUserId,
        senderName: senderName,
        messageType: messageType,
        message: notificationMessage,
        attachmentCount: attachments ? attachments.length : 0,
        hasImage: notificationImage ? 'Yes' : 'No',
        oneSignalId: oneSignalResult.id,
        timestamp: new Date().toISOString()
      });

      // Return success response
      return new Response(
        JSON.stringify({ 
          success: true, 
          message: 'Enhanced notification sent successfully',
          oneSignalId: oneSignalResult.id,
          notificationDetails: {
            title: notificationTitle,
            content: notificationContent,
            hasImage: !!notificationImage,
            attachmentCount: attachments ? attachments.length : 0
          }
        }), 
        { 
          status: 200,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
          }
        }
      );

    } catch (error) {
      console.error('Worker error:', error);
      return new Response(
        JSON.stringify({ 
          error: 'Internal server error',
          message: error.message 
        }), 
        { 
          status: 500,
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
          }
        }
      );
    }
  },
};
