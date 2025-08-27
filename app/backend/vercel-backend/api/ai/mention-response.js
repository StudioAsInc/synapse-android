// Vercel API endpoint for handling AI mention responses
import { GoogleGenerativeAI } from '@google/generative-ai';

export default async function handler(req, res) {
  // Only allow POST requests
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { message, userId, userName, context, botUserId } = req.body;

    // Validate request
    if (!message || !userId) {
      return res.status(400).json({ error: 'Missing required fields' });
    }

    // Security: Only process if from authorized bot account
    if (botUserId !== process.env.SYRA_BOT_UID) {
      return res.status(403).json({ error: 'Unauthorized bot account' });
    }

    // Initialize Gemini AI
    const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
    const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

    // Generate AI response
    const prompt = `You are Syra, a friendly AI assistant integrated into Synapse social media. 
                   You were mentioned by ${userName || 'a user'} who said: "${message}"
                   
                   Context: ${context || 'General conversation'}
                   
                   Respond naturally as if you're joining the conversation. Keep it conversational, 
                   helpful, and engaging. Use appropriate emojis and maintain a positive tone.
                   Keep response under 150 words.`;

    const result = await model.generateContent(prompt);
    const response = result.response;
    const aiMessage = response.text();

    // Return the AI response
    res.status(200).json({
      success: true,
      response: aiMessage,
      timestamp: Date.now()
    });

  } catch (error) {
    console.error('AI Response Error:', error);
    
    // Return fallback response on error
    const fallbackResponses = [
      "Hey there! 👋 Thanks for mentioning me!",
      "Hi! I'm here and ready to chat! 😊",
      "Hello! What's on your mind? ✨",
      "Hey! Great to see you here! 🤖"
    ];
    
    const fallback = fallbackResponses[Math.floor(Math.random() * fallbackResponses.length)];
    
    res.status(200).json({
      success: true,
      response: fallback,
      fallback: true,
      timestamp: Date.now()
    });
  }
}