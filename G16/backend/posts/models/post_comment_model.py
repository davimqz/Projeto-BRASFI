from django.db import models
from accounts.models.member_model import Member
from posts.models.post_model import Post
from accounts.models.base_entity import BaseEntity

class PostComment(BaseEntity):
    post = models.ForeignKey(
        Post,
        on_delete=models.CASCADE,
        related_name='comments',
        help_text='Post relacionado ao comentário'
    )
    author = models.ForeignKey(
        Member,
        on_delete=models.CASCADE,
        related_name='comments',
        help_text='Membro autor do comentário'
    )
    content = models.TextField(help_text='Conteúdo do comentário')
    edited = models.BooleanField(default=False, help_text='Comentário foi editado')
    edited_at = models.DateTimeField(null=True, blank=True)

    def __str__(self):
        return f'Comentário de {self.author.username} no post {self.post.id}'

    class Meta:
        verbose_name = 'Comentário'
        verbose_name_plural = 'Comentários'
