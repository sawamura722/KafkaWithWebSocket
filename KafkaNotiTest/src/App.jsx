import { useState, useEffect } from 'react'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import './App.css'

function App() {
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState('')
  const [stompClient, setStompClient] = useState(null)
  const [connected, setConnected] = useState(false)

  // Connect to WebSocket
  useEffect(() => {
    const connectWebSocket = () => {
      const socket = new SockJS('http://localhost:8080/ws')
      const client = new Client({
        webSocketFactory: () => socket,
        onConnect: () => {
          console.log('Connected to WebSocket')
          setConnected(true)
          
          // Subscribe to topic
          client.subscribe('/topic/martial_art', (message) => {
            const receivedMessage = message.body
            console.log('Received message:', receivedMessage)
            setMessages(prevMessages => [...prevMessages, receivedMessage])
          })
          
          // STEP 1: Set up listener for the response
          // Subscribe to user-specific queue for message history
          client.subscribe('/user/queue/history', (message) => {
            console.log('Received history:', message.body)
            const history = JSON.parse(message.body)
            setMessages(history)
          })
          
          // STEP 2: Send the request
          // Request message history through WebSocket instead of HTTP
          client.publish({
            destination: '/app/fetch-history',
            body: JSON.stringify({ limit: 50 }) // Optional parameters
          })
        },
        onDisconnect: () => {
          console.log('Disconnected from WebSocket')
          setConnected(false)
        },
        onError: (error) => {
          console.error('WebSocket error:', error)
        }
      })
      
      client.activate()
      setStompClient(client)
    }
    
    connectWebSocket()
    
    // Cleanup on unmount
    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.deactivate()
      }
    }
  }, [])
  
  // Send a new message
  const sendMessage = async () => {
    if (!newMessage.trim()) return
    
    if (stompClient && stompClient.connected) {
      stompClient.publish({
        destination: '/app/send-message',
        body: newMessage
      })
      setNewMessage('')
    }
  }

  // WebSocket-based refresh function
  const refreshViaWebSocket = () => {
    if (stompClient && stompClient.connected) {
      stompClient.publish({
        destination: '/app/fetch-history',
        body: JSON.stringify({ limit: 50 })
      });
    }
  }

  return (
    <div className="parent-container">
      <div className="app-container">
      <h1>Kafka Notification Test</h1>
      
      <div className="connection-status">
        Status: {connected ? 
          <span className="connected">Connected</span> : 
          <span className="disconnected">Disconnected</span>
        }
      </div>
      
      <div className="message-form">
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Enter a message"
        />
        <button onClick={sendMessage}>Send Message</button>
      </div>
      
      <div className="messages-container">
        <h2>Messages</h2>
        <button onClick={refreshViaWebSocket}>Refresh Messages</button>
        <ul>
          {messages.length > 0 ? (
            messages.map((message, index) => (
              <li key={index}>{message}</li>
            ))
          ) : (
            <li className="no-messages">No messages yet</li>
          )}
        </ul>
      </div>
    </div>
    </div>
  )
}

export default App