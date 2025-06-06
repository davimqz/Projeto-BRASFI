import React, { useEffect, useState } from 'react';
import { chatService } from '../../services/chatService';
import type { Chat } from '../../types/chat';
import './ChatList.css';

interface ChatListProps {
  onSelectChat: (chat: Chat) => void;
}

export const ChatList: React.FC<ChatListProps> = ({ onSelectChat }) => {
  const [chats, setChats] = useState<Chat[]>([]);

  useEffect(() => {
    const fetchChats = async () => {
      try {
        const chatList = await chatService.listChats();
        setChats(chatList);
      } catch (error) {
        console.error('Erro ao carregar chats:', error);
      }
    };

    fetchChats();
  }, []);

  return (
    <div className="chat-list">
      <h2 className="chat-list-title">Chats</h2>
      <ul className="chat-list-items">
        {chats.map((chat) => (
          <li key={chat.id} className="chat-list-item">
            <button 
              className="chat-list-button"
              onClick={() => onSelectChat(chat)}
            >
              <span className="chat-name">Chat {chat.id}</span>
              <span className="chat-type">Tipo: {chat.type}</span>
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}; 