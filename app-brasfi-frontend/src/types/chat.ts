export interface Chat {
  id: number;
  type: string;
  createdAt: string;
}

export interface ChatParticipant {
  id: number;
  chatId: number;
  memberId: number;
}

export interface Message {
  id: number;
  chatId: number;
  senderId: number;
  content: string;
  timestamp: string;
}

export interface ChatDto {
  type: string;
}

export interface MessageDto {
  chatId: number;
  senderId: number;
  content: string;
} 