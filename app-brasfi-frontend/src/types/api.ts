export interface Author {
  id: number;
  name: string;
  username: string;
  // Adicione outros campos se forem retornados e necessários
}

export interface Post {
  id: number;
  content: string;
  mediaUrl?: string;
  createdAt: string; // Vem como string do backend (ISO DateTime), precisará ser formatada
  author: Author;
}

export interface Page<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    // Adicione aqui outros campos de pageable que a API do Spring Data JPA retorna, se precisar deles.
    // Ex: sort, offset, paged, unpaged
  };
  last: boolean;        // true se esta é a última página
  totalPages: number;   // número total de páginas
  totalElements: number;// número total de elementos em todas as páginas
  size: number;         // tamanho da página requisitado
  number: number;       // número da página atual (base 0)
  first: boolean;       // true se esta é a primeira página
  numberOfElements: number; // número de elementos na página atual
  empty: boolean;       // true se a página atual está vazia
}

export interface Community {
  id: number;
  name: string;
  // Adicione outros campos se forem retornados e necessários no futuro
  // description?: string; 
} 