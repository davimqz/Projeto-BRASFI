import React, { useEffect, useState, useRef } from 'react';
import { chatService } from '../../services/chatService';
import type { Chat, Message, MessageDto } from '../../types/chat';
import './ChatMessages.css';

interface ChatMessagesProps {
  chat: Chat;
  currentUserId: number;
}

export const ChatMessages: React.FC<ChatMessagesProps> = ({ chat, currentUserId }) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const messageList = await chatService.getMessages(chat.id);
        setMessages(messageList);
        scrollToBottom();
      } catch (error) {
        console.error('Erro ao carregar mensagens:', error);
      }
    };

    fetchMessages();
  }, [chat.id]);

  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    const messageDto: MessageDto = {
      chatId: chat.id,
      senderId: currentUserId,
      content: newMessage.trim()
    };

    try {
      const sentMessage = await chatService.sendMessage(messageDto);
      setMessages([...messages, sentMessage]);
      setNewMessage('');
      scrollToBottom();
    } catch (error) {
      console.error('Erro ao enviar mensagem:', error);
    }
  };

  return (
    <div className="chat-messages">
      <div className="messages-container">
        {messages.map((message) => (
          <div
            key={message.id}
            className={`message ${message.senderId === currentUserId ? 'sent' : 'received'}`}
          >
            <div className="message-content">{message.content}</div>
            <div className="message-time">
              {new Date(message.timestamp).toLocaleTimeString()}
            </div>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>
      
      <form className="message-form" onSubmit={handleSendMessage}>
        <input
          type="text"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Digite sua mensagem..."
          className="message-input"
        />
        <button type="submit" className="send-button">
          Enviar
        </button>
      </form>
    </div>
  );
}; 