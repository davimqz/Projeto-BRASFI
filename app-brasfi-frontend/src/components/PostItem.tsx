import React, { useState, useEffect } from 'react';
import type { Post } from '../types/api'; // Corrigido para type-only import
import '../styles/postItem.css'; // Criaremos este CSS

interface PostItemProps {
  post: Post;
  currentUserId?: number | null; // ID do usuário logado
  onRequestDelete: (postId: number) => void; // Função para solicitar a exclusão
  onSaveEdit: (postId: number, newContent: string, newMediaUrl: string, authorId: number) => Promise<boolean>; // Retorna Promise<boolean> para indicar sucesso/falha
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

const PostItem: React.FC<PostItemProps> = ({ post, currentUserId, onRequestDelete, onSaveEdit }) => {
  const [showOptionsMenu, setShowOptionsMenu] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editableContent, setEditableContent] = useState(post.content);
  const [editableMediaUrl, setEditableMediaUrl] = useState(post.mediaUrl || '');
  const [editError, setEditError] = useState<string | null>(null);
  const [isSavingEdit, setIsSavingEdit] = useState<boolean>(false);

  // Sincronizar estados editáveis se o post prop mudar (ex: após salvar e receber post atualizado)
  useEffect(() => {
    setEditableContent(post.content);
    setEditableMediaUrl(post.mediaUrl || '');
  }, [post.content, post.mediaUrl]);

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

  const handleEditClick = (event: React.MouseEvent) => {
    event.stopPropagation();
    setEditableContent(post.content);
    setEditableMediaUrl(post.mediaUrl || '');
    setEditError(null); // Limpar erros de edições anteriores
    setIsEditing(true);
    setShowOptionsMenu(false);
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setEditError(null);
    // Resetar para valores originais do post (já feito pelo useEffect se post prop não mudou)
    setEditableContent(post.content);
    setEditableMediaUrl(post.mediaUrl || '');
  };

  const handleSaveClick = async () => {
    if (!editableContent.trim()) {
      setEditError("O conteúdo não pode ficar vazio.");
      return;
    }
    setIsSavingEdit(true);
    setEditError(null);
    const success = await onSaveEdit(post.id, editableContent, editableMediaUrl, post.author.id);
    setIsSavingEdit(false);
    if (success) {
      setIsEditing(false);
    } else {
      // Erro será tratado em Home.tsx e exibido lá, ou podemos ter um estado de erro aqui também
      setEditError("Falha ao salvar a edição. Tente novamente."); // Exemplo de erro local
    }
  };

  // Fecha o menu de opções se clicar fora dele (dentro do post-item)
  const handlePostItemClick = () => {
    if (showOptionsMenu) {
      setShowOptionsMenu(false);
    }
  };

  return (
    <div className="post-item" onClick={handlePostItemClick}>
      <div className="post-header">
        <div className="post-author-info">
          <span className="post-author-name">{post.author.name}</span>
          <span className="post-username">@{post.author.username}</span>
        </div>
        <div className="post-meta">
          <span className="post-timestamp">{formatDate(post.createdAt)}</span>
          {isAuthor && (
            <div className="post-options-container">
              <button onClick={toggleOptionsMenu} className="post-options-button" aria-label="Opções do post">
                ⋮ 
              </button>
              {showOptionsMenu && (
                <div className="post-options-menu">
                  <button onClick={handleEditClick} className="edit-option">Editar</button>
                  <button onClick={handleDeleteClick} className="delete-option">Excluir</button>
                </div>
              )}
            </div>
          )}
        </div>
      </div>

      {isEditing ? (
        <div className="post-edit-mode">
          <textarea 
            value={editableContent}
            onChange={(e) => setEditableContent(e.target.value)}
            className="edit-content-textarea"
            rows={5}
          />
          <input 
            type="url"
            value={editableMediaUrl}
            onChange={(e) => setEditableMediaUrl(e.target.value)}
            placeholder="URL da Mídia (opcional)"
            className="edit-media-input"
          />
          {editError && <p className="error-message edit-error-local">{editError}</p>}
          <div className="edit-actions">
            <button onClick={handleCancelEdit} className="cancel-edit-button" disabled={isSavingEdit}>Cancelar</button>
            <button onClick={handleSaveClick} className="save-edit-button" disabled={isSavingEdit}>
              {isSavingEdit ? 'Salvando...' : 'Salvar Alterações'}
            </button>
          </div>
        </div>
      ) : (
        <div className="post-content">
          <p>{post.content}</p>
          {post.mediaUrl && (
            <div className="post-media">
              {/* Simplesmente assumindo que é uma imagem por enquanto */}
              <img src={post.mediaUrl} alt="Mídia do post" />
            </div>
          )}
        </div>
      )}
      {/* Adicionar aqui interações como curtidas, comentários, etc. no futuro */}
    </div>
  );
};

export default PostItem; 