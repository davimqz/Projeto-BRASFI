from rest_framework import serializers
from .models import Follow, DirectMessage
from users.serializers import UserSerializer

class FollowSerializer(serializers.ModelSerializer):
    follower = UserSerializer(read_only=True)
    following = UserSerializer(read_only=True)

    class Meta:
        model = Follow
        fields = ('id', 'follower', 'following', 'created_at')
        read_only_fields = ('id', 'follower', 'created_at')

class DirectMessageSerializer(serializers.ModelSerializer):
    sender = UserSerializer(read_only=True)
    receiver = UserSerializer(read_only=True)

    class Meta:
        model = DirectMessage
        fields = ('id', 'sender', 'receiver', 'content', 'image',
                 'is_read', 'created_at')
        read_only_fields = ('id', 'sender', 'created_at') 