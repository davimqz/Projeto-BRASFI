from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as DjangoUserAdmin
from django.utils.translation import gettext_lazy as _

from .models import Member, Background, CommunityLeader, Administrator


@admin.register(Member)
class MemberAdmin(DjangoUserAdmin):
    model = Member
    list_display = ('username', 'email', 'first_name', 'last_name', 'is_active', 'is_staff')
    list_filter = ('is_active', 'is_staff', 'groups')
    search_fields = ('username', 'email', 'cpf', 'first_name', 'last_name')
    ordering = ('username',)
    filter_horizontal = ('groups', 'user_permissions', 'backgrounds')

    fieldsets = (
        (None, {'fields': ('username', 'password')}),
        (_('Personal info'), {'fields': ('first_name', 'last_name', 'email', 'cpf', 'phone', 'description')}),
        (_('Backgrounds'), {'fields': ('backgrounds',)}),
        (_('Permissions'), {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        (_('Important dates'), {'fields': ('last_login', 'date_joined')}),
    )
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('username', 'email', 'cpf', 'first_name', 'last_name', 'password1', 'password2'),
        }),
    )


@admin.register(Background)
class BackgroundAdmin(admin.ModelAdmin):
    list_display = ('title', 'institution', 'background_type', 'start_year', 'end_year')
    list_filter = ('background_type',)
    search_fields = ('title', 'institution')


@admin.register(CommunityLeader)
class CommunityLeaderAdmin(admin.ModelAdmin):
    list_display = ('username', 'email', 'is_active')
    list_filter = ('groups',)
    search_fields = ('username', 'email')
    ordering = ('username',)


@admin.register(Administrator)
class AdministratorAdmin(admin.ModelAdmin):
    list_display = ('username', 'email', 'is_active')
    list_filter = ('groups',)
    search_fields = ('username', 'email')
    ordering = ('username',)
