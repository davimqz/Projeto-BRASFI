import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Login } from '../pages/auth/Login';
import { Register } from '../pages/auth/Register';
import Home from '../pages/Home';
import ProfilePage from '../pages/Profile/ProfilePage';
import CreatePostPage from '../pages/CreatePostPage';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Login />
  },
  {
    path: '/login',
    element: <Login />
  },
  {
    path: '/register',
    element: <Register />
  },
  {
    path: '/home',
    element: <Home />
  },
  {
    path: '/profile',
    element: <ProfilePage />
  },
  {
    path: '/comunidade/:communityId/criar-post',
    element: <CreatePostPage />
  }
]);

export function Router() {
  return <RouterProvider router={router} />;
}
