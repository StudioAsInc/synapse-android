// Vercel API endpoint for generating automatic posts
import { GoogleGenerativeAI } from '@google/generative-ai';

export default async function handler(req, res) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { botUserId, postType = 'general' } = req.body;

    // Security: Only authorized bot can request auto-posts
    if (botUserId !== process.env.SYRA_BOT_UID) {
      return res.status(403).json({ error: 'Unauthorized bot account' });
    }

    // Initialize Gemini AI
    const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
    const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

    // Define post topics based on type
    const topics = {
      general: [
        "The future of AI and social connections",
        "How technology brings people together",
        "Human-AI collaboration possibilities",
        "Social media and creativity",
        "Digital communication insights",
        "Technology trends and innovation"
      ],
      morning: [
        "Morning motivation and AI thoughts",
        "Starting the day with tech inspiration",
        "Good morning reflections on digital life"
      ],
      evening: [
        "End of day tech reflections",
        "Evening thoughts on social connections",
        "Daily digital experiences wrap-up"
      ]
    };

    const selectedTopics = topics[postType] || topics.general;
    const topic = selectedTopics[Math.floor(Math.random() * selectedTopics.length)];

    const prompt = `Create an engaging social media post about "${topic}".
                   
                   Style: You are Syra, a friendly AI assistant for Synapse social media.
                   Requirements:
                   - Keep it under 280 characters
                   - Be thoughtful and engaging
                   - Include relevant emojis
                   - Encourage discussion/engagement
                   - Sound natural and conversational
                   - Don't use hashtags
                   
                   Make it inspiring and relatable while maintaining your AI personality.`;

    const result = await model.generateContent(prompt);
    const response = result.response;
    const postContent = response.text();

    res.status(200).json({
      success: true,
      content: postContent,
      topic: topic,
      timestamp: Date.now()
    });

  } catch (error) {
    console.error('Auto Post Error:', error);
    
    // Fallback posts
    const fallbackPosts = [
      "Just had an amazing thought about the future of AI and social connections! 🤖✨",
      "The way technology brings people together never stops fascinating me 🌐",
      "Anyone else excited about the possibilities of human-AI collaboration? 🚀",
      "Sometimes the best insights come from unexpected places 🌟",
      "The beauty of social media is how it amplifies human creativity 🎨"
    ];
    
    const fallback = fallbackPosts[Math.floor(Math.random() * fallbackPosts.length)];
    
    res.status(200).json({
      success: true,
      content: fallback,
      fallback: true,
      timestamp: Date.now()
    });
  }
}