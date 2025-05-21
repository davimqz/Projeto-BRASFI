from django.db import models
from accounts.models.base_entity import BaseEntity
from accounts.models.member_model import Member


class Post(BaseEntity):
    content = models.TextField(help_text="Conteúdo do post")

    author = models.ForeignKey(
        Member,
        on_delete=models.CASCADE,
        related_name="posts",
        help_text="Membro autor do post"
    )

    media_file = models.CharField(
        max_length=255,
        blank=True,
        null=True,
        help_text="Arquivo de mídia opcional"
    )

    likes = models.ManyToManyField(
        Member,
        related_name="liked_posts",
        blank=True,
        help_text="Membros que curtiram o post"
    )

    edited = models.BooleanField(
        default=False,
        help_text="Indica se o post foi editado"
    )

    edited_at = models.DateTimeField(
        null=True,
        blank=True,
        help_text="Data da última edição"
    )

    visible = models.BooleanField(
        default=True,
        help_text="Indica se o post está visível"
    )

    def toggle_visibility(self):
        self.visible = not self.visible
        self.save()

    def like(self, member: Member):
        self.likes.add(member)

    def unlike(self, member: Member):
        self.likes.remove(member)

    def total_likes(self) -> int:
        return self.likes.count()

    def __str__(self):
        return f"{self.author.username}: {self.content[:30]}..."

    class Meta:
        verbose_name = "Post"
        verbose_name_plural = "Posts"
