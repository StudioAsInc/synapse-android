# Synapse AI Bot Backend

This directory contains the Vercel serverless backend for the Synapse AI Bot.

## 📁 Structure

```
backend/
└── vercel-backend/          # Vercel serverless functions
    ├── api/                 # API endpoints
    │   ├── ai/             # AI processing endpoints
    │   └── webhooks/       # Firebase webhooks
    ├── public/             # Static files (API tester)
    ├── package.json        # Dependencies
    └── vercel.json         # Vercel configuration
```

## 🚀 Quick Start

1. **Install dependencies:**
```bash
cd vercel-backend
npm install
```

2. **Run locally:**
```bash
vercel dev
```

3. **Deploy to production:**
```bash
vercel --prod
```

## 🔧 Configuration

Set these environment variables in Vercel:
- `GEMINI_API_KEY` - Your Google Gemini API key
- `SYRA_BOT_UID` - DxSt08c8VfVjSQWCj3UGgMSeBVb2
- `WEBHOOK_SECRET` - Secret for webhook authentication

## 📖 Documentation

See [VERCEL_DEPLOYMENT_GUIDE.md](../../VERCEL_DEPLOYMENT_GUIDE.md) for detailed setup instructions.

## 🔗 API Endpoints

- `POST /api/ai/mention-response` - Handle @syra mentions
- `POST /api/ai/auto-post` - Generate automatic posts  
- `POST /api/ai/comment-generation` - Generate comments
- `POST /api/webhooks/firebase-trigger` - Firebase webhooks

## 🧪 Testing

Visit `/public/index.html` after deployment to test APIs in browser.