// Test script for the notification worker
// Run this with: node test-worker.js

const { fetch } = require('node-fetch');

// Mock environment for testing
const env = {
  ONESIGNAL_REST_API_KEY: 'test-key-for-testing'
};

// Mock request
const testRequest = {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    recipientUserId: 'test-player-id-123',
    notificationMessage: 'This is a test notification message'
  })
};

// Test the worker logic
async function testWorker() {
  console.log('🧪 Testing Notification Worker...\n');
  
  try {
    // Test CORS preflight
    console.log('1. Testing CORS preflight...');
    const corsRequest = { method: 'OPTIONS' };
    const corsResponse = await worker.fetch(corsRequest, env);
    console.log(`   CORS Response Status: ${corsResponse.status}`);
    console.log(`   CORS Headers: ${JSON.stringify(Object.fromEntries(corsResponse.headers.entries()))}\n`);
    
    // Test invalid method
    console.log('2. Testing invalid method...');
    const invalidRequest = { method: 'GET' };
    const invalidResponse = await worker.fetch(invalidRequest, env);
    console.log(`   Invalid Method Response Status: ${invalidResponse.status}\n`);
    
    // Test missing fields
    console.log('3. Testing missing fields...');
    const missingFieldsRequest = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ recipientUserId: 'test-id' })
    };
    const missingFieldsResponse = await worker.fetch(missingFieldsRequest, env);
    const missingFieldsText = await missingFieldsResponse.text();
    console.log(`   Missing Fields Response Status: ${missingFieldsResponse.status}`);
    console.log(`   Missing Fields Response: ${missingFieldsText}\n`);
    
    // Test valid request
    console.log('4. Testing valid request...');
    const validResponse = await worker.fetch(testRequest, env);
    const validText = await validResponse.text();
    console.log(`   Valid Request Response Status: ${validResponse.status}`);
    console.log(`   Valid Request Response: ${validText}\n`);
    
    console.log('✅ All tests completed!');
    
  } catch (error) {
    console.error('❌ Test failed:', error);
  }
}

// Import the worker (you'll need to modify this path)
// const worker = require('./worker.js');

console.log('⚠️  Note: This test script requires the worker to be imported.');
console.log('   You may need to modify the import statement based on your setup.\n');

// Uncomment the line below to run tests when the worker is properly imported
// testWorker();