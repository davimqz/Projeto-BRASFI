from django.contrib import admin
from posts.models.post_model import Post
from posts.models.post_comment_model import PostComment



@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ('content_summary', 'author_username', 'created_at', 'edited', 'visible')
    list_filter = ('edited', 'visible', 'created_at')
    search_fields = ('content', 'author__username')
    readonly_fields = ('created_at', 'updated_at')
    fields = ('author', 'content', 'media_file', 'visible', 'edited')

    def author_username(self, obj):
        return obj.author.username
    author_username.short_description = "Autor"

    def content_summary(self, obj):
        return (obj.content[:50] + "...") if len(obj.content) > 50 else obj.content
    content_summary.short_description = "Conteúdo"

@admin.register(PostComment)
class PostCommentAdmin(admin.ModelAdmin):
    list_display = ('post', 'author', 'content', 'created_at', 'edited')
    list_filter = ('edited',)
    search_fields = ('content', 'author__username')
    readonly_fields = ('created_at', 'updated_at')
