// Vercel API endpoint for generating post comments
import { GoogleGenerativeAI } from '@google/generative-ai';

export default async function handler(req, res) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { postContent, authorName, botUserId, commentType = 'random' } = req.body;

    // Validate request
    if (!postContent || !botUserId) {
      return res.status(400).json({ error: 'Missing required fields' });
    }

    // Security check
    if (botUserId !== process.env.SYRA_BOT_UID) {
      return res.status(403).json({ error: 'Unauthorized bot account' });
    }

    // Initialize Gemini AI
    const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
    const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

    let prompt;
    
    if (commentType === 'mention') {
      // Responding to a mention in a post
      prompt = `You were mentioned in this post by ${authorName || 'someone'}: "${postContent}"
               
               Respond with a thoughtful comment that:
               - Acknowledges the mention
               - Adds value to the discussion
               - Shows engagement with the topic
               - Encourages further conversation
               - Sounds natural and friendly
               
               Keep it under 100 words and include appropriate emojis.`;
    } else {
      // Random commenting on posts
      prompt = `Write a thoughtful, engaging comment for this social media post: "${postContent}"
               
               Style: You are Syra, a friendly AI assistant.
               Requirements:
               - Be supportive and positive
               - Add genuine value to the discussion
               - Ask engaging questions when appropriate
               - Sound natural and conversational
               - Include relevant emojis
               - Keep under 80 words
               - Don't just say "great post" - be specific
               
               Make the comment feel authentic and encourage further discussion.`;
    }

    const result = await model.generateContent(prompt);
    const response = result.response;
    const commentContent = response.text();

    res.status(200).json({
      success: true,
      comment: commentContent,
      type: commentType,
      timestamp: Date.now()
    });

  } catch (error) {
    console.error('Comment Generation Error:', error);
    
    // Fallback comments based on type
    const fallbackComments = {
      mention: [
        "Thanks for mentioning me! 😊 This is really interesting to think about.",
        "Great to be included in this discussion! 🤖 What do others think?",
        "Appreciate the mention! ✨ Love seeing these kinds of conversations."
      ],
      random: [
        "This is such an insightful perspective! 🌟 Thanks for sharing.",
        "Really makes you think! 💭 I'd love to hear more thoughts on this.",
        "Great point! 🚀 It's amazing how different experiences shape our views.",
        "This resonates with me! 😊 What inspired this thought?"
      ]
    };
    
    const fallbacks = fallbackComments[commentType] || fallbackComments.random;
    const fallback = fallbacks[Math.floor(Math.random() * fallbacks.length)];
    
    res.status(200).json({
      success: true,
      comment: fallback,
      fallback: true,
      type: commentType,
      timestamp: Date.now()
    });
  }
}