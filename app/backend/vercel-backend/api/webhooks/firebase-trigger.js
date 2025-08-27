// Vercel webhook endpoint for Firebase database triggers
import { GoogleGenerativeAI } from '@google/generative-ai';

export default async function handler(req, res) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { eventType, data, auth } = req.body;

    // Verify webhook authenticity
    if (auth?.token !== process.env.WEBHOOK_SECRET) {
      return res.status(401).json({ error: 'Unauthorized webhook' });
    }

    console.log('Firebase trigger received:', eventType, data);

    switch (eventType) {
      case 'message_mention':
        await handleMessageMention(data);
        break;
        
      case 'post_mention':
        await handlePostMention(data);
        break;
        
      case 'auto_post_trigger':
        await handleAutoPost(data);
        break;
        
      case 'random_comment_trigger':
        await handleRandomComment(data);
        break;
        
      default:
        console.log('Unknown event type:', eventType);
    }

    res.status(200).json({ success: true, processed: eventType });

  } catch (error) {
    console.error('Webhook processing error:', error);
    res.status(500).json({ error: 'Webhook processing failed' });
  }
}

async function handleMessageMention(data) {
  const { chatId, messageText, senderId, senderName } = data;
  
  try {
    // Generate AI response
    const response = await fetch(`${process.env.VERCEL_URL}/api/ai/mention-response`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        message: messageText,
        userId: senderId,
        userName: senderName,
        context: 'chat',
        botUserId: process.env.SYRA_BOT_UID
      })
    });

    const aiResponse = await response.json();
    
    if (aiResponse.success) {
      // Send message back to Firebase via admin SDK or API
      await sendChatMessage(chatId, aiResponse.response);
    }
  } catch (error) {
    console.error('Error handling message mention:', error);
  }
}

async function handlePostMention(data) {
  const { postId, postText, authorId, authorName } = data;
  
  try {
    // Generate comment response
    const response = await fetch(`${process.env.VERCEL_URL}/api/ai/comment-generation`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        postContent: postText,
        authorName: authorName,
        commentType: 'mention',
        botUserId: process.env.SYRA_BOT_UID
      })
    });

    const aiResponse = await response.json();
    
    if (aiResponse.success) {
      // Add comment to Firebase
      await addCommentToPost(postId, aiResponse.comment);
    }
  } catch (error) {
    console.error('Error handling post mention:', error);
  }
}

async function handleAutoPost(data) {
  try {
    // Generate auto post
    const response = await fetch(`${process.env.VERCEL_URL}/api/ai/auto-post`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        botUserId: process.env.SYRA_BOT_UID,
        postType: data.timeOfDay || 'general'
      })
    });

    const aiResponse = await response.json();
    
    if (aiResponse.success) {
      // Create post in Firebase
      await createPost(aiResponse.content);
    }
  } catch (error) {
    console.error('Error handling auto post:', error);
  }
}

async function handleRandomComment(data) {
  const { postId, postText, authorName } = data;
  
  try {
    // Generate random comment
    const response = await fetch(`${process.env.VERCEL_URL}/api/ai/comment-generation`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        postContent: postText,
        authorName: authorName,
        commentType: 'random',
        botUserId: process.env.SYRA_BOT_UID
      })
    });

    const aiResponse = await response.json();
    
    if (aiResponse.success) {
      // Add comment to Firebase
      await addCommentToPost(postId, aiResponse.comment);
    }
  } catch (error) {
    console.error('Error handling random comment:', error);
  }
}

// Helper functions for Firebase operations
async function sendChatMessage(chatId, message) {
  // Implement Firebase Admin SDK call or HTTP API call
  console.log(`Sending chat message to ${chatId}: ${message}`);
}

async function addCommentToPost(postId, comment) {
  // Implement Firebase Admin SDK call or HTTP API call
  console.log(`Adding comment to post ${postId}: ${comment}`);
}

async function createPost(content) {
  // Implement Firebase Admin SDK call or HTTP API call
  console.log(`Creating auto post: ${content}`);
}