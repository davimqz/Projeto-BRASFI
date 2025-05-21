from posts.models.post_model import Post
from accounts.models.member_model import Member
from django.shortcuts import get_object_or_404


class PostDAO:

    @staticmethod
    def create(author: Member, content: str, media_file: str = None) -> Post:
        return Post.objects.create(author=author, content=content, media_file=media_file)

    @staticmethod
    def find_by_id(post_id: str) -> Post:
        return get_object_or_404(Post, id=post_id)

    @staticmethod
    def delete(post: Post):
        post.delete()

    @staticmethod
    def save(post: Post):
        post.save()
