from typing import Optional
from posts.models.post_model import Post
from accounts.models.member_model import Member
from django.db import transaction


class PostDAO:
    @staticmethod
    def create(author: Member, content: str, media_file: Optional[str] = None) -> Post:
        return Post.objects.create(author=author, content=content, media_file=media_file)

    @staticmethod
    def get_by_id(post_id: str) -> Optional[Post]:
        return Post.objects.filter(id=post_id, deleted=False).first()

    @staticmethod
    def list_all_visible():
        return Post.objects.filter(deleted=False, visible=True).order_by("-created_at")

    @staticmethod
    def update_content(post: Post, new_content: str, edited_at) -> None:
        post.content = new_content
        post.edited = True
        post.edited_at = edited_at
        post.save()

    @staticmethod
    def hide(post: Post) -> None:
        post.visible = False
        post.save()

    @staticmethod
    def soft_delete(post: Post) -> None:
        post.deleted = True
        post.save()

    @staticmethod
    def restore(post: Post) -> None:
        post.deleted = False
        post.save()
