#!/bin/bash

# Deployment script for Cloudflare Worker
# This script helps deploy the notification worker to Cloudflare

echo "🚀 Deploying Notification Worker to Cloudflare..."

# Check if wrangler is installed
if ! command -v wrangler &> /dev/null; then
    echo "❌ Wrangler CLI not found. Installing..."
    npm install -g wrangler
fi

# Check if user is logged in
if ! wrangler whoami &> /dev/null; then
    echo "❌ Not logged in to Cloudflare. Please run: wrangler login"
    exit 1
fi

# Set environment
ENV=${1:-production}
echo "📦 Deploying to environment: $ENV"

# Deploy the worker
echo "🔧 Deploying worker..."
if [ "$ENV" = "staging" ]; then
    wrangler deploy --env staging
elif [ "$ENV" = "production" ]; then
    wrangler deploy --env production
else
    wrangler deploy
fi

if [ $? -eq 0 ]; then
    echo "✅ Worker deployed successfully!"
    echo ""
    echo "📋 Next steps:"
    echo "1. Set your OneSignal REST API key:"
    echo "   wrangler secret put ONESIGNAL_REST_API_KEY --env $ENV"
    echo ""
    echo "2. Test the worker with:"
    echo "   curl -X POST https://your-worker.your-subdomain.workers.dev \\"
    echo "     -H 'Content-Type: application/json' \\"
    echo "     -d '{\"recipientUserId\":\"test-id\",\"notificationMessage\":\"Test message\"}'"
    echo ""
    echo "3. Update your Android app's NotificationHelper.kt with the new worker URL"
else
    echo "❌ Deployment failed!"
    exit 1
fi