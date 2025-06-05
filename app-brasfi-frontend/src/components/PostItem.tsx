import React from 'react';
import type { Post } from '../types/api'; // Corrigido para type-only import
import '../styles/postItem.css'; // Criaremos este CSS

interface PostItemProps {
  post: Post;
}

// Função simples para formatar a data (pode ser melhorada ou substituída por uma lib)
const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  };
  try {
    return new Date(dateString).toLocaleDateString(undefined, options);
  } catch (e) {
    console.error("Erro ao formatar data:", e); // Logar o erro
    return dateString; // fallback se a data for inválida
  }
};

const PostItem: React.FC<PostItemProps> = ({ post }) => {
  return (
    <div className="post-item">
      <div className="post-header">
        <span className="post-author-name">{post.author.name}</span>
        <span className="post-username">@{post.author.username}</span>
        <span className="post-timestamp">{formatDate(post.createdAt)}</span>
      </div>
      <div className="post-content">
        <p>{post.content}</p>
        {post.mediaUrl && (
          <div className="post-media">
            {/* Simplesmente assumindo que é uma imagem por enquanto */}
            <img src={post.mediaUrl} alt="Mídia do post" />
          </div>
        )}
      </div>
      {/* Adicionar aqui interações como curtidas, comentários, etc. no futuro */}
    </div>
  );
};

export default PostItem; 