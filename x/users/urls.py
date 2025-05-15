from django.urls import path
from knox import views as knox_views
from .views import (
    UserCreateView,
    UserListView,
    UserDetailView,
    UserProfileView,
    FollowUserView,
    LoginView
)

urlpatterns = [
    path('login/', LoginView.as_view(), name='login'),
    path('register/', UserCreateView.as_view(), name='register'),
    path('users/', UserListView.as_view(), name='user-list'),
    path('users/me/', UserDetailView.as_view(), name='user-detail'),
    path('users/<int:pk>/', UserProfileView.as_view(), name='user-profile'),
    path('users/<int:user_id>/follow/', FollowUserView.as_view(), name='follow-user'),
    path('logout/', knox_views.LogoutView.as_view(), name='knox_logout'),
    path('logoutall/', knox_views.LogoutAllView.as_view(), name='knox_logoutall'),
] 