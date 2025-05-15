from django.core.exceptions import ObjectDoesNotExist, PermissionDenied
from django.db import transaction
from datetime import datetime

from posts.dao.post_dao import PostDAO
from posts.models.post_model import Post
from accounts.models.member_model import Member


class PostService:
    @staticmethod
    @transaction.atomic
    def create_post(author: Member, content: str, media_file: str = None) -> Post:
        if not content.strip():
            raise ValueError("O conteúdo do post não pode estar vazio.")
        return PostDAO.create(author, content.strip(), media_file)

    @staticmethod
    def get_post(post_id: str) -> Post:
        post = PostDAO.get_by_id(post_id)
        if not post:
            raise ObjectDoesNotExist("Post não encontrado.")
        return post

    @staticmethod
    @transaction.atomic
    def edit_post(requester: Member, post_id: str, new_content: str) -> None:
        post = PostDAO.get_by_id(post_id)
        if not post:
            raise ObjectDoesNotExist("Post não encontrado.")
        if post.author.id != requester.id:
            raise PermissionDenied("Você não pode editar um post que não é seu.")
        PostDAO.update_content(post, new_content.strip(), datetime.now())

    @staticmethod
    @transaction.atomic
    def hide_post(post_id: str, requester: Member) -> None:
        post = PostDAO.get_by_id(post_id)
        if not post:
            raise ObjectDoesNotExist("Post não encontrado.")
        if post.author.id != requester.id:
            raise PermissionDenied("Você não pode ocultar um post que não é seu.")
        PostDAO.hide(post)

    @staticmethod
    def list_posts():
        return PostDAO.list_all_visible()

    @staticmethod
    @transaction.atomic
    def delete_post(post_id: str, requester: Member) -> None:
        post = PostDAO.get_by_id(post_id)
        if not post:
            raise ObjectDoesNotExist("Post não encontrado.")
        if post.author.id != requester.id:
            raise PermissionDenied("Você não pode deletar um post que não é seu.")
        PostDAO.soft_delete(post)
