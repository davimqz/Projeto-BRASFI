import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/global.css';
import '../styles/createPostPage.css'; // Criaremos este arquivo em seguida

const API_URL = '/api';

export default function CreatePostPage() {
  const { communityId } = useParams<{ communityId: string }>();
  const navigate = useNavigate();
  const [content, setContent] = useState('');
  const [mediaUrl, setMediaUrl] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [communityName, setCommunityName] = useState<string | null>(null); // Opcional: buscar nome da comunidade

  // Opcional: Buscar dados da comunidade para exibir o nome
  useEffect(() => {
    const fetchCommunityDetails = async () => {
      if (communityId) {
        const token = localStorage.getItem('token');
        try {
          // Assumindo que existe um endpoint para buscar uma comunidade por ID
          const response = await axios.get(`${API_URL}/community/${communityId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (response.data && response.data.name) {
            setCommunityName(response.data.name);
          }
        } catch (err) {
          console.error("Erro ao buscar detalhes da comunidade:", err);
          // Se não conseguir buscar, o título usará apenas o ID
        }
      }
    };
    fetchCommunityDetails();
  }, [communityId]);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);

    if (!content.trim()) {
      setError('O conteúdo da publicação não pode estar vazio.');
      return;
    }

    const authorId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    if (!authorId || !token) {
      setError('Erro de autenticação. Faça login novamente.');
      // Poderia redirecionar para login aqui
      return;
    }

    setIsLoading(true);
    try {
      await axios.post(
        `${API_URL}/post`,
        {
          content: content.trim(),
          mediaUrl: mediaUrl.trim() || null, // Envia null se vazio
          authorId: parseInt(authorId, 10),
          communityId: parseInt(communityId!, 10),
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      // Navega para Home e passa o communityId para que a Home possa recarregar os posts
      navigate('/home', { state: { newPostInCommunityId: parseInt(communityId!, 10) } });
    } catch (err) {
      console.error("Erro ao criar post:", err);
      setError('Falha ao criar a publicação. Tente novamente.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoBack = () => {
    // Navega para Home e passa o communityId para que a Home possa selecionar a comunidade correta
    navigate('/home', { state: { selectCommunityId: parseInt(communityId!, 10) } });
  };

  return (
    <div className="create-post-page-container">
      <header className="create-post-header">
        <button onClick={handleGoBack} className="back-button">
          &larr; Voltar
        </button>
        <h1>
          Criar Nova Publicação em {communityName || `Comunidade #${communityId}`}
        </h1>
      </header>
      <main className="create-post-main-content">
        <form onSubmit={handleSubmit} className="create-post-form">
          <div className="form-group">
            <label htmlFor="post-content">Conteúdo:</label>
            <textarea
              id="post-content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="O que você está pensando?"
              rows={8}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="post-media-url">URL da Mídia (Opcional):</label>
            <input
              type="url"
              id="post-media-url"
              value={mediaUrl}
              onChange={(e) => setMediaUrl(e.target.value)}
              placeholder="https://exemplo.com/imagem.png"
            />
          </div>
          {error && <p className="error-message">{error}</p>}
          <button type="submit" className="submit-button" disabled={isLoading}>
            {isLoading ? 'Publicando...' : 'Publicar'}
          </button>
        </form>
      </main>
    </div>
  );
} 