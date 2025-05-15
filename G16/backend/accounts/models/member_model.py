import uuid
from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.contrib.auth.base_user import BaseUserManager
from django.core.validators import RegexValidator
from django.core.mail import send_mail
from django.utils import timezone
from .base_entity import BaseEntity, BaseEntityManager


class MemberManager(BaseEntityManager, BaseUserManager):
    """
    Manager that filters out soft-deleted members by default and handles user creation.
    """
    def create_user(self, username, email, cpf, first_name, last_name, password=None, **extra_fields):
        if not username:
            raise ValueError('The Username field is required')
        if not email:
            raise ValueError('The Email field is required')
        if not cpf:
            raise ValueError('The CPF field is required')
        email = self.normalize_email(email)
        username = username.lower()
        user = self.model(
            username=username,
            email=email,
            cpf=cpf,
            first_name=first_name,
            last_name=last_name,
            **extra_fields
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, username, email, cpf, first_name, last_name, password=None, **extra_fields):
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)

        if not extra_fields.get('is_staff'):
            raise ValueError('Superuser must have is_staff=True.')
        if not extra_fields.get('is_superuser'):
            raise ValueError('Superuser must have is_superuser=True.')

        return self.create_user(username, email, cpf, first_name, last_name, password, **extra_fields)


class Member(BaseEntity, AbstractBaseUser, PermissionsMixin):
    """
    Custom user (Member) model.
    Inherits BaseEntity for common fields (timestamps, UUID, soft-delete),
    AbstractBaseUser for auth, and PermissionsMixin for permissions.
    """
    username = models.CharField(
        max_length=150,
        unique=True,
        help_text='Unique username, lowercase enforced'
    )
    email = models.EmailField(
        unique=True,
        help_text='User email address'
    )
    cpf = models.CharField(
        max_length=14,
        unique=True,
        validators=[
            RegexValidator(
                regex=r'^\d{3}\.\d{3}\.\d{3}-\d{2}$',
                message='CPF must be in XXX.XXX.XXX-XX format'
            )
        ],
        help_text='CPF in the format XXX.XXX.XXX-XX'
    )
    first_name = models.CharField(
        max_length=30,
        help_text='First name of the member'
    )
    last_name = models.CharField(
        max_length=30,
        help_text='Last name of the member'
    )
    phone = models.CharField(
        max_length=15,
        blank=True,
        null=True,
        help_text='Contact phone number, include country code if needed'
    )
    description = models.TextField(
        blank=True,
        help_text='Brief description or bio'
    )

    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)

    backgrounds = models.ManyToManyField(
        'Background',
        related_name='members',
        blank=True,
        help_text='Educational and professional backgrounds'
    )

    objects = MemberManager()
    all_objects = models.Manager()

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['email', 'cpf', 'first_name', 'last_name']

    def save(self, *args, **kwargs):
        # Normalize email and username
        self.email = self.email.lower()
        self.username = self.username.lower()
        super().save(*args, **kwargs)

    @property
    def date_joined(self):
        """Alias for created_at from BaseEntity"""
        return self.created_at

    def get_full_name(self):
        """Return the member's full name."""
        return f"{self.first_name} {self.last_name}".strip()

    def get_short_name(self):
        """Return the member's first name."""
        return self.first_name

    @property
    def initials(self):
        """Return initials (e.g., 'JD' for John Doe)."""
        if self.first_name and self.last_name:
            return (self.first_name[0] + self.last_name[0]).upper()
        return ''

    @property
    def is_administrator(self):
        """Check if the member belongs to the Administrator group."""
        return self.groups.filter(name='Administrator').exists()

    @property
    def is_community_leader(self):
        """Check if the member belongs to the CommunityLeader group."""
        return self.groups.filter(name='CommunityLeader').exists()

    def email_user(self, subject, message, from_email=None, **kwargs):
        """Send an email to this member."""
        send_mail(subject, message, from_email, [self.email], **kwargs)

    def add_background(self, background):
        """Associate a Background with this member."""
        self.backgrounds.add(background)

    def remove_background(self, background):
        """Remove a Background association from this member."""
        self.backgrounds.remove(background)

    def clear_backgrounds(self):
        """Clear all Background associations."""
        self.backgrounds.clear()

    def __str__(self):
        return self.username

    class Meta:
        verbose_name = 'Member'
        verbose_name_plural = 'Members'


class BackgroundType(models.TextChoices):
    ACADEMIC = 'ACADEMIC', 'Academic'
    PROFESSIONAL = 'PROFESSIONAL', 'Professional'


class Background(BaseEntity):
    """
    Educational or professional Background entry for a Member.
    """
    title = models.CharField(
        max_length=100,
        help_text='Title or degree name'
    )
    institution = models.CharField(
        max_length=100,
        help_text='Name of the educational or professional institution'
    )
    background_type = models.CharField(
        max_length=12,
        choices=BackgroundType.choices,
        help_text='Type of background: academic or professional'
    )
    start_year = models.PositiveIntegerField(
        help_text='Start year of this background entry'
    )
    end_year = models.PositiveIntegerField(
        help_text='End year or graduation year'
    )

    def __str__(self):
        return f"{self.title} at {self.institution} ({self.get_background_type_display()})"

    class Meta:
        verbose_name = 'Background'
        verbose_name_plural = 'Backgrounds'
        ordering = ['-start_year']


class CommunityLeader(Member):
    """
    Proxy model for members with community leadership permissions.
    """
    class Meta:
        proxy = True
        verbose_name = 'Community Leader'
        verbose_name_plural = 'Community Leaders'

    def create_community(self, name: str, description: str) -> 'communities.models.Community':
        from communities.services import CommunityService
        return CommunityService.create_community(self, name, description)

    def change_leader(self, community: 'communities.models.Community', new_leader: 'CommunityLeader') -> None:
        from communities.services import CommunityService
        CommunityService.change_leader(self, community, new_leader)

    def approve_member(self, member: Member, community: 'communities.models.Community') -> None:
        from communities.services import CommunityService
        CommunityService.approve_member(self, member, community)

    def unlock_member(self, member: Member, community: 'communities.models.Community') -> None:
        from communities.services import CommunityService
        CommunityService.unlock_member(self, member, community)

    def restrict_member_in_community(self, member: Member, community: 'communities.models.Community') -> None:
        from communities.services import CommunityService
        CommunityService.restrict_member(self, member, community)

    def delete_community(self, community: 'communities.models.Community') -> None:
        from communities.services import CommunityService
        CommunityService.delete_community(self, community)

    def send_community_invite(self, invite: 'communities.models.Invite', member: Member, community: 'communities.models.Community') -> None:
        from communities.services import CommunityService
        CommunityService.send_invite(self, invite, member, community)

    def resend_invite(self, invite: 'communities.models.Invite') -> None:
        from communities.services import CommunityService
        CommunityService.resend_invite(self, invite)

    def cancel_invite(self, invite: 'communities.models.Invite') -> None:
        from communities.services import CommunityService
        CommunityService.cancel_invite(self, invite)


class Administrator(CommunityLeader):
    """
    Proxy model for administrators, with elevated permissions.
    """
    class Meta:
        proxy = True
        verbose_name = 'Administrator'
        verbose_name_plural = 'Administrators'

    def promote_to_leader(self, member: Member) -> CommunityLeader:
        from accounts.services.user_service import UserService
        return UserService.promote_to_leader(self, member)

    def demote_leader(self, leader: CommunityLeader) -> None:
        from accounts.services.user_service import UserService
        UserService.demote_leader(self, leader)

    def reset_password_for(self, member: Member, new_password: str) -> None:
        member.set_password(new_password)
        member.save()

    def ban_member(self, member: Member) -> None:
        from accounts.services.user_service import UserService
        UserService.ban_member(self, member)

    def restrict_member(self, member: Member) -> None:
        from accounts.services.user_service import UserService
        UserService.restrict_member(self, member)
