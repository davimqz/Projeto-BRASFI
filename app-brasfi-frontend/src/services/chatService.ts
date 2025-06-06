import axios from 'axios';
import type { Chat, ChatDto, ChatParticipant, Message, MessageDto } from '../types/chat';

const API_URL = 'http://localhost:8080';

export const chatService = {
  // Chat operations
  createChat: async (dto: ChatDto): Promise<Chat> => {
    const response = await axios.post(`${API_URL}/chat`, dto);
    return response.data;
  },

  getChat: async (id: number): Promise<Chat> => {
    const response = await axios.get(`${API_URL}/chat/${id}`);
    return response.data;
  },

  listChats: async (): Promise<Chat[]> => {
    const response = await axios.get(`${API_URL}/chat`);
    return response.data;
  },

  updateChat: async (id: number, dto: ChatDto): Promise<Chat> => {
    const response = await axios.put(`${API_URL}/chat/${id}`, dto);
    return response.data;
  },

  deleteChat: async (id: number): Promise<void> => {
    await axios.delete(`${API_URL}/chat/${id}`);
  },

  // Chat participants operations
  addParticipant: async (chatId: number, memberId: number): Promise<ChatParticipant> => {
    const response = await axios.post(`${API_URL}/chat/${chatId}/participant/${memberId}`);
    return response.data;
  },

  removeParticipant: async (chatId: number, memberId: number): Promise<void> => {
    await axios.delete(`${API_URL}/chat/${chatId}/participant/${memberId}`);
  },

  listParticipants: async (chatId: number): Promise<ChatParticipant[]> => {
    const response = await axios.get(`${API_URL}/chat/${chatId}/participant`);
    return response.data;
  },

  // Messages operations
  sendMessage: async (message: MessageDto): Promise<Message> => {
    const response = await axios.post(`${API_URL}/message`, message);
    return response.data;
  },

  getMessages: async (chatId: number): Promise<Message[]> => {
    const response = await axios.get(`${API_URL}/message/chat/${chatId}`);
    return response.data;
  }
}; 