import React, { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import logo from '../assets/round_logo.png';
import profilePhoto from '../assets/profile_photo_sample.png';
import '../styles/global.css'; // Importando estilos globais
import '../styles/home.css';   // Importando estilos específicos da Home
import { Link, useLocation, useNavigate } from 'react-router-dom'; // Importar Link e useLocation/useNavigate
import type { Post, Page as ApiPage, Community } from '../types/api'; // Importar tipos
import PostItem from '../components/PostItem'; // Importar PostItem
import ConfirmationModal from '../components/ConfirmationModal'; // Importar o Modal

const API_URL = '/api'; // Consistente com ProfilePage
const POSTS_PER_PAGE = 10; // Definir quantos posts carregar por vez

export default function Home() {
  const [userName, setUserName] = useState<string | null>(null);
  const [currentUserId, setCurrentUserId] = useState<number | null>(null);
  const location = useLocation(); // Hook para acessar location.state
  const navigate = useNavigate(); // Hook para navegação programática

  // Estados para comunidades
  const [communities, setCommunities] = useState<Community[]>([]);
  const [communitiesError, setCommunitiesError] = useState<string | null>(null);
  const [isLoadingCommunities, setIsLoadingCommunities] = useState<boolean>(false);

  // Estados para o feed
  const [selectedCommunityId, setSelectedCommunityId] = useState<number | null>(null);
  const [posts, setPosts] = useState<Post[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(0); // A página atual da API (base 0)
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [hasMorePosts, setHasMorePosts] = useState<boolean>(true);
  const [feedError, setFeedError] = useState<string | null>(null);

  // Estados para o modal de exclusão
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState<boolean>(false);
  const [postIdToDelete, setPostIdToDelete] = useState<number | null>(null);
  const [deleteError, setDeleteError] = useState<string | null>(null);
  const [isDeletingPost, setIsDeletingPost] = useState<boolean>(false);

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
    const token = localStorage.getItem('token');
    const userIdStr = localStorage.getItem('userId');
    if (userIdStr) {
      setCurrentUserId(parseInt(userIdStr, 10));
    }

    const fetchUserName = async () => {
      if (token && userIdStr) {
        try {
          const response = await axios.get(`${API_URL}/member/${userIdStr}`, {
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

    const fetchCommunities = async () => {
      setIsLoadingCommunities(true);
      setCommunitiesError(null);
      if (token) {
        try {
          const response = await axios.get<Community[]>(`${API_URL}/community`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          setCommunities(response.data);
        } catch (error) {
          console.error("Erro ao buscar comunidades:", error);
          setCommunitiesError("Falha ao carregar a lista de comunidades. Tente recarregar a página.");
        } finally {
          setIsLoadingCommunities(false);
        }
      } else {
        setCommunitiesError("Usuário não autenticado.");
        setIsLoadingCommunities(false);
      }
    };

    fetchUserName();
    fetchCommunities();
  }, []);

  // useEffect para lidar com o retorno da CreatePostPage
  useEffect(() => {
    if (location.state) {
      const { newPostInCommunityId, selectCommunityId } = location.state as { newPostInCommunityId?: number, selectCommunityId?: number };
      
      let communityToProcess: number | null = null;

      if (newPostInCommunityId !== undefined) {
        communityToProcess = newPostInCommunityId;
      } else if (selectCommunityId !== undefined) {
        communityToProcess = selectCommunityId;
      }

      if (communityToProcess !== null) {
        // Se a comunidade já estiver selecionada e for a mesma de newPostInCommunityId, 
        // precisamos forçar a recarga chamando handleCommunityClick com uma pequena alteração
        // ou simplificando: sempre chamar handleCommunityClick, pois ele já reseta e busca.
        // A lógica atual do handleCommunityClick já previne recarga desnecessária se o ID for o mesmo e tiver posts,
        // então precisamos garantir que ele *sempre* recarregue se newPostInCommunityId estiver presente.
        if (newPostInCommunityId !== undefined && communityToProcess === selectedCommunityId) {
            // Forçar a recarga para a mesma comunidade para ver o novo post
            setPosts([]); 
            setCurrentPage(0); 
            setHasMorePosts(true); 
            setFeedError(null); 
            fetchPosts(communityToProcess, 0);
        } else {
            handleCommunityClick(communityToProcess);
        }
        // Limpa o state da navegação para evitar recargas em loop
        navigate(location.pathname, { replace: true, state: {} });
      }
    }
  }, [location.state, navigate, selectedCommunityId]); // Adicionar selectedCommunityId como dependência para a lógica de forçar recarga

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
    // Modificado para permitir recarga forçada se location.state indicou um novo post
    // A verificação original continua válida para cliques normais do usuário.
    if (selectedCommunityId === communityId && posts.length > 0 && !location.state?.newPostInCommunityId) {
      return; 
    }

    setSelectedCommunityId(communityId);
    setPosts([]); 
    setCurrentPage(0); 
    setHasMorePosts(true); 
    setFeedError(null); 
    fetchPosts(communityId, 0);
  };

  const handleCreatePost = () => {
    if (selectedCommunityId) {
      navigate(`/comunidade/${selectedCommunityId}/criar-post`);
    }
  };

  const handleRequestDelete = (postId: number) => {
    setPostIdToDelete(postId);
    setDeleteError(null); // Limpa erros anteriores
    setIsDeleteModalOpen(true);
  };

  const handleCancelDelete = () => {
    setIsDeleteModalOpen(false);
    setPostIdToDelete(null);
    setDeleteError(null);
  };

  const handleConfirmDeletePost = async () => {
    if (!postIdToDelete) return;
    const token = localStorage.getItem('token');
    if (!token) {
      setDeleteError("Erro de autenticação. Faça login novamente.");
      return;
    }
    setIsDeletingPost(true);
    setDeleteError(null);
    try {
      await axios.delete(`${API_URL}/post/${postIdToDelete}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPosts(prevPosts => prevPosts.filter(post => post.id !== postIdToDelete));
      handleCancelDelete(); // Fecha o modal e reseta estados
    } catch (error) {
      console.error("Erro ao deletar post:", error);
      setDeleteError("Falha ao excluir a publicação. Tente novamente.");
    } finally {
      setIsDeletingPost(false);
    }
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
          {/* Lista de comunidades */}
          <ul className="community-list">
            {isLoadingCommunities && <li className="community-item loading">Carregando comunidades...</li>}
            {communitiesError && <li className="community-item error">{communitiesError}</li>}
            {!isLoadingCommunities && !communitiesError && communities.length === 0 && (
              <li className="community-item">Nenhuma comunidade encontrada.</li>
            )}
            {!isLoadingCommunities && !communitiesError && communities.map(community => (
              <li 
                key={community.id}
                className={`community-item ${selectedCommunityId === community.id ? 'active' : ''}`}
                onClick={() => handleCommunityClick(community.id)}
              >
                {community.name}
              </li>
            ))}
          </ul>
        </div>

        {/* Conteúdo principal da página */}
        <div className="content">
          {!selectedCommunityId && (
            <h1 className="header">Selecione uma comunidade para ver o feed.</h1>
          )}

          {selectedCommunityId && (
            <>
              <div className="create-post-button-container">
                <button onClick={handleCreatePost} className="create-post-button">
                  Criar Nova Publicação
                </button>
              </div>
              <div className="feed-container">
                {posts.map(post => (
                  <PostItem 
                    key={post.id} 
                    post={post} 
                    currentUserId={currentUserId} 
                    onRequestDelete={handleRequestDelete} 
                  />
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
            </>
          )}
        </div>
      </div>
      <ConfirmationModal
        isOpen={isDeleteModalOpen}
        title="Confirmar Exclusão"
        message={`Tem certeza que deseja excluir esta publicação? ${deleteError ? 
          '<br/><strong style="color: red;">' + deleteError + '</strong>' : ''}`}
        confirmText="Sim, Excluir"
        cancelText="Não"
        onConfirm={handleConfirmDeletePost}
        onCancel={handleCancelDelete}
        isLoading={isDeletingPost}
      />
    </div>
  );
}
