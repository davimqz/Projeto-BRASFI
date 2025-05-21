from posts.dao.post_dao import PostDAO
from posts.models.post_model import Post
from accounts.models.member_model import Member
from django.utils import timezone


class PostService:

    @staticmethod
    def create_post(author: Member, content: str, media_file: str = None) -> Post:
        return PostDAO.create(author, content.strip(), media_file)

    @staticmethod
    def edit_post(post_id: str, new_content: str, editor: Member) -> Post:
        post = PostDAO.find_by_id(post_id)
        if post.author != editor:
            raise PermissionError("Você não tem permissão para editar este post.")
        post.content = new_content.strip()
        post.edited = True
        post.edited_at = timezone.now()
        PostDAO.save(post)
        return post

    @staticmethod
    def delete_post(post_id: str, requester: Member):
        post = PostDAO.find_by_id(post_id)
        if post.author != requester:
            raise PermissionError("Você não tem permissão para excluir este post.")
        PostDAO.delete(post)

    @staticmethod
    def toggle_visibility(post_id: str, requester: Member):
        post = PostDAO.find_by_id(post_id)
        if post.author != requester:
            raise PermissionError("Você não pode alterar a visibilidade deste post.")
        post.toggle_visibility()

    @staticmethod
    def like_post(post_id: str, member: Member):
        post = PostDAO.find_by_id(post_id)
        post.like(member)

    @staticmethod
    def unlike_post(post_id: str, member: Member):
        post = PostDAO.find_by_id(post_id)
        post.unlike(member)

    @staticmethod
    def count_likes(post_id: str) -> int:
        post = PostDAO.find_by_id(post_id)
        return post.total_likes()
