import React, { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import logo from '../assets/round_logo.png';
import profilePhoto from '../assets/profile_photo_sample.png';
import '../styles/global.css'; // Importando estilos globais
import '../styles/home.css';   // Importando estilos específicos da Home
import { Link } from 'react-router-dom'; // Importar Link
import type { Post, Page as ApiPage } from '../types/api'; // Importar tipos
import PostItem from '../components/PostItem'; // Importar PostItem

const API_URL = '/api'; // Consistente com ProfilePage
const POSTS_PER_PAGE = 10; // Definir quantos posts carregar por vez

export default function Home() {
  const [userName, setUserName] = useState<string | null>(null);

  // Estados para o feed
  const [selectedCommunityId, setSelectedCommunityId] = useState<number | null>(null);
  const [posts, setPosts] = useState<Post[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(0); // A página atual da API (base 0)
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [hasMorePosts, setHasMorePosts] = useState<boolean>(true);
  const [feedError, setFeedError] = useState<string | null>(null);

  // Ref para o observer da rolagem infinita
  const observer = useRef<IntersectionObserver | null>(null);
  const loadMoreRef = useCallback((node: HTMLDivElement | null) => {
    if (isLoading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMorePosts && selectedCommunityId !== null) {
        // Chama fetchPosts para a *próxima* página lógica
        // A API espera a próxima página (currentPage é a última carregada)
        fetchPosts(selectedCommunityId, currentPage + 1); 
      }
    });
    if (node) observer.current.observe(node);
  }, [isLoading, hasMorePosts, selectedCommunityId, currentPage]);

  useEffect(() => {
    const fetchUserName = async () => {
      const token = localStorage.getItem('token');
      const userId = localStorage.getItem('userId');

      if (token && userId) {
        try {
          const response = await axios.get(`${API_URL}/member/${userId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (response.data && response.data.name) {
            const fullName = response.data.name;
            const firstName = fullName.split(' ')[0]; // Pega o primeiro nome
            setUserName(firstName);
          }
        } catch (error) {
          console.error("Erro ao buscar nome do usuário para Home:", error);
          // Não precisa mostrar erro na UI da Home, apenas loga
        }
      }
    };
    fetchUserName();
  }, []);

  const fetchPosts = async (communityId: number, pageToFetch: number) => {
    if (isLoading && pageToFetch !== 0) return; // Evitar múltiplas chamadas de scroll enquanto uma já carrega, exceto para o clique inicial
    setIsLoading(true);
    setFeedError(null);
    const token = localStorage.getItem('token');

    try {
      const response = await axios.get<ApiPage<Post>>(
        `${API_URL}/post/community/${communityId}?page=${pageToFetch}&size=${POSTS_PER_PAGE}&sort=createdAt,desc`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      
      const fetchedPosts = response.data.content;
      
      setPosts(prevPosts => (pageToFetch === 0 ? fetchedPosts : [...prevPosts, ...fetchedPosts]));
      setCurrentPage(response.data.number); // Atualiza a página atual com o que a API retornou
      setHasMorePosts(!response.data.last);

    } catch (error) {
      console.error(`Erro ao buscar posts da comunidade ${communityId}, página ${pageToFetch}:`, error);
      setFeedError('Falha ao carregar posts. Tente novamente mais tarde.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCommunityClick = (communityId: number) => {
    if (selectedCommunityId === communityId && posts.length > 0) return; // Não recarrega se já selecionada e com posts

    setSelectedCommunityId(communityId);
    setPosts([]); 
    setCurrentPage(0); 
    setHasMorePosts(true); 
    setFeedError(null); 
    fetchPosts(communityId, 0); // Buscar a primeira página (página 0)
  };

  return (
    <div className="home-container">
      {/* Header Superior */}
      <header className="top-header">
        <div className="logo-container-header">
          <img src={logo} alt="Logo Brasfi" className="logo-header" />
          <h2 className="logo-name-header">Brasfi</h2>
        </div>
        <div className="user-greeting-avatar-container"> {/* Novo container para agrupar saudação e avatar */}
          {userName && <span className="user-greeting">Olá, {userName}</span>}
          <Link to="/profile" className="avatar-link">
            <img src={profilePhoto} alt="Avatar do usuário" className="avatar-placeholder" />
          </Link>
        </div>
      </header>

      <div className="main-content-area"> {/* Novo container para sidebar e content */}
        <div className="sidebar">
          {/* Cabeçalho: logo + nome - Removido daqui, movido para o top-header */}
          {/* 
          <div className="logo-container">
            <img src={logo} alt="Logo da Empresa" className="logo" />
            <h2 className="logo-name">Brasfi</h2>
          </div> 
          */}

          {/* Lista de comunidades */}
          <ul className="community-list">
            <li 
              className={`community-item ${selectedCommunityId === 1 ? 'active' : ''}`}
              onClick={() => handleCommunityClick(1)} // Assumindo ID 1 para "Comunidade 1"
            >
              Comunidade 1
            </li>
            {/* Outras comunidades podem ser adicionadas aqui no futuro */}
            {/* Exemplo:
            <li 
              className={`community-item ${selectedCommunityId === 2 ? 'active' : ''}`}
              onClick={() => handleCommunityClick(2)}
            >
              Comunidade 2
            </li>
            */}
          </ul>
        </div>

        {/* Conteúdo principal da página */}
        <div className="content">
          {!selectedCommunityId && (
            <h1 className="header">Selecione uma comunidade para ver o feed.</h1>
          )}

          {selectedCommunityId && (
            <div className="feed-container">
              {posts.map(post => (
                <PostItem key={post.id} post={post} />
              ))}
              
              {hasMorePosts && !isLoading && (
                <div ref={loadMoreRef} className="load-more-trigger">
                  {/* Pode-se adicionar um botão "Carregar mais" aqui como fallback ou alternativa */}
                </div>
              )}

              {isLoading && <p className="feed-status-message">Carregando posts...</p>}
              
              {!isLoading && !hasMorePosts && posts.length > 0 && (
                <p className="feed-status-message">Você chegou ao fim dos posts.</p>
              )}
              {!isLoading && posts.length === 0 && !hasMorePosts && selectedCommunityId && (
                 <p className="feed-status-message">Nenhum post encontrado nesta comunidade.</p>
              )}
              {feedError && <p className="feed-status-message error-message">{feedError}</p>}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
