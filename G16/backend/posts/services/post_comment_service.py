from posts.dao.post_comment_dao import PostCommentDAO
from posts.models.post_model import Post
from accounts.models.member_model import Member

class PostCommentService:
    @staticmethod
    def add_comment(post: Post, author: Member, content: str):
        return PostCommentDAO.create(post, author, content.strip())

    @staticmethod
    def get_comments(post: Post):
        return PostCommentDAO.list_by_post(post)

    @staticmethod
    def delete_comment(comment_id: str):
        PostCommentDAO.delete(comment_id)
