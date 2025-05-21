from posts.models.post_comment_model import PostComment


class PostCommentDAO:
    @staticmethod
    def create(post, author, content):
        return PostComment.objects.create(post=post, author=author, content=content)

    @staticmethod
    def list_by_post(post):
        return PostComment.objects.filter(post=post).order_by('-created_at')

    @staticmethod
    def delete(comment_id):
        PostComment.objects.filter(id=comment_id).delete()
