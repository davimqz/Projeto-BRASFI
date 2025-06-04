import React, { useState } from 'react';
import { ChatList } from './ChatList';
import { ChatMessages } from './ChatMessages';
import type { Chat as ChatType } from '../../types/chat';
import './Chat.css';

interface ChatProps {
  currentUserId: number;
}

export const Chat: React.FC<ChatProps> = ({ currentUserId }) => {
  const [selectedChat, setSelectedChat] = useState<ChatType | null>(null);

  return (
    <div className="chat-container">
      <div className="chat-sidebar">
        <ChatList onSelectChat={setSelectedChat} />
      </div>
      <div className="chat-main">
        {selectedChat ? (
          <ChatMessages chat={selectedChat} currentUserId={currentUserId} />
        ) : (
          <div className="no-chat-selected">
            <p>Selecione um chat para começar a conversar</p>
          </div>
        )}
      </div>
    </div>
  );
}; 