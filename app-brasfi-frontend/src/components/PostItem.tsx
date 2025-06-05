import React, { useState } from 'react';
import type { Post } from '../types/api'; // Corrigido para type-only import
import '../styles/postItem.css'; // Criaremos este CSS

interface PostItemProps {
  post: Post;
  currentUserId?: number | null; // ID do usuário logado
  onRequestDelete: (postId: number) => void; // Função para solicitar a exclusão
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

const PostItem: React.FC<PostItemProps> = ({ post, currentUserId, onRequestDelete }) => {
  const [showOptionsMenu, setShowOptionsMenu] = useState(false);

  const isAuthor = currentUserId && post.author && currentUserId === post.author.id;

  const toggleOptionsMenu = (event: React.MouseEvent) => {
    event.stopPropagation(); // Evita que o clique feche o menu imediatamente se ele estiver em um elemento pai com onClick
    setShowOptionsMenu(!showOptionsMenu);
  };

  const handleDeleteClick = (event: React.MouseEvent) => {
    event.stopPropagation();
    onRequestDelete(post.id);
    setShowOptionsMenu(false); // Fecha o menu após clicar
  };

  return (
    <div className="post-item" onClick={() => { if (showOptionsMenu) setShowOptionsMenu(false); }}>
      <div className="post-header">
        <div className="post-author-info">
          <span className="post-author-name">{post.author.name}</span>
          <span className="post-username">@{post.author.username}</span>
        </div>
        <div className="post-meta">
          <span className="post-timestamp">{formatDate(post.createdAt)}</span>
          {isAuthor && (
            <div className="post-options-container">
              <button onClick={toggleOptionsMenu} className="post-options-button">
                ⋮ 
              </button>
              {showOptionsMenu && (
                <div className="post-options-menu">
                  <button onClick={handleDeleteClick} className="delete-option">
                    Excluir
                  </button>
                </div>
              )}
            </div>
          )}
        </div>
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