export default {
  async fetch(request, env, ctx) {
    if (request.method !== 'POST') {
      return new Response('Method Not Allowed', { status: 405 });
    }

    try {
      const { recipientId, message, senderName } = await request.json();

      if (!recipientId || !message || !senderName) {
        return new Response('Missing required fields: recipientId, message, senderName', { status: 400 });
      }

      const ONESIGNAL_APP_ID = env.ONESIGNAL_APP_ID;
      const ONESIGNAL_REST_API_KEY = env.ONESIGNAL_REST_API_KEY;

      if (!ONESIGNAL_APP_ID || !ONESIGNAL_REST_API_KEY) {
        console.error('OneSignal credentials are not set in the environment variables.');
        return new Response('Internal Server Error: OneSignal credentials not configured.', { status: 500 });
      }

      const notification = {
        app_id: ONESIGNAL_APP_ID,
        contents: { en: message },
        headings: { en: `New message from ${senderName}` },
        include_external_user_ids: [recipientId],
      };

      const onesignalRequest = new Request('https://onesignal.com/api/v1/notifications', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json; charset=utf-8',
          'Authorization': `Basic ${ONESIGNAL_REST_API_KEY}`,
        },
        body: JSON.stringify(notification),
      });

      const response = await fetch(onesignalRequest);
      const responseBody = await response.json();

      if (response.ok) {
        return new Response(JSON.stringify(responseBody), {
          status: 200,
          headers: { 'Content-Type': 'application/json' },
        });
      } else {
        console.error('OneSignal API error:', response.status, responseBody);
        return new Response(JSON.stringify(responseBody), {
          status: response.status,
          headers: { 'Content-Type': 'application/json' },
        });
      }
    } catch (error) {
      console.error('Error processing request:', error);
      return new Response('Internal Server Error', { status: 500 });
    }
  },
};
