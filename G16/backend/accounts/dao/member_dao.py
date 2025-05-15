from typing import Optional
from django.db.models import QuerySet
from django.contrib.auth.models import Group
from django.db import transaction
from django.core.mail import send_mail
from accounts.models.member_model import Member, Background


class MemberDAO:
    """
    Data Access Object for Member entities.
    Provides methods for CRUD operations, background associations,
    existence checks (username/email/CPF),
    support for filtering and pagination,
    and audit retrieval bypassing soft-delete.
    """
    @staticmethod
    def get_by_id(member_id: str) -> Optional[Member]:
        """Retrieve a non-deleted member by ID."""
        return Member.objects.filter(id=member_id).first()

    @staticmethod
    def get_all_by_id(member_id: str) -> Optional[Member]:
        """Retrieve a member by ID regardless of soft-delete."""
        return Member.all_objects.filter(id=member_id).first()

    @staticmethod
    def get_by_username(username: str) -> Optional[Member]:
        """Retrieve a non-deleted member by username."""
        return Member.objects.filter(username=username.lower()).first()

    @staticmethod
    def get_by_email(email: str) -> Optional[Member]:
        """Retrieve a non-deleted member by email."""
        return Member.objects.filter(email=email.lower()).first()

    @staticmethod
    def get_by_cpf(cpf: str) -> Optional[Member]:
        """Retrieve a non-deleted member by CPF."""
        return Member.objects.filter(cpf=cpf).first()

    @staticmethod
    def exists_by_username(username: str) -> bool:
        """Return True if a member with the given username exists (non-deleted)."""
        return Member.objects.filter(username=username.lower()).exists()

    @staticmethod
    def exists_by_email(email: str) -> bool:
        """Return True if a member with the given email exists (non-deleted)."""
        return Member.objects.filter(email=email.lower()).exists()

    @staticmethod
    def exists_by_cpf(cpf: str) -> bool:
        """Return True if a member with the given CPF exists (non-deleted)."""
        return Member.objects.filter(cpf=cpf).exists()

    @staticmethod
    def list_all(
        is_active: Optional[bool] = None,
        background_id: Optional[str] = None,
        page: int = 1,
        page_size: int = 20,
        order_by: str = '-created_at'
    ) -> QuerySet[Member]:
        """
        Return a paginated, optionally filtered list of non-deleted members.
        """
        qs = Member.objects.all().order_by(order_by)
        if is_active is not None:
            qs = qs.filter(is_active=is_active)
        if background_id:
            qs = qs.filter(backgrounds__id=background_id)
        offset = (page - 1) * page_size
        return qs[offset:offset + page_size]

    @staticmethod
    def create_member(**kwargs) -> Member:
        """
        Atomically create a member and send a welcome email.
        Expects: username, email, cpf, first_name, last_name, password, etc.
        """
        with transaction.atomic():
            member = Member.objects.create_user(**kwargs)
            try:
                send_mail(
                    subject='Welcome!',
                    message=f'Hello {member.get_full_name()}, welcome aboard!',
                    from_email=None,
                    recipient_list=[member.email],
                    fail_silently=True,
                )
            except Exception:
                pass
        return member

    @staticmethod
    def update_member(member: Member, **fields) -> Member:
        """Update fields on a member and save."""
        for attr, value in fields.items():
            setattr(member, attr, value)
        member.save()
        return member

    @staticmethod
    def soft_delete(member: Member) -> None:
        """Soft-delete a member."""
        member.delete()

    @staticmethod
    def hard_delete(member: Member) -> None:
        """Permanently delete a member."""
        member.hard_delete()

    @staticmethod
    def restore(member: Member) -> None:
        """Restore a soft-deleted member."""
        member.restore()

    @staticmethod
    def add_background(member: Member, background: Background) -> None:
        """Associate a background with the member."""
        member.backgrounds.add(background)

    @staticmethod
    def remove_background(member: Member, background: Background) -> None:
        """Remove a background association from the member."""
        member.backgrounds.remove(background)


class BackgroundDAO:
    """
    DAO for Background entities.
    Provides CRUD operations and filtering/pagination support.
    """
    @staticmethod
    def get_by_id(background_id: str) -> Optional[Background]:
        return Background.objects.filter(id=background_id).first()

    @staticmethod
    def list_all(
        background_type: Optional[str] = None,
        page: int = 1,
        page_size: int = 20
    ) -> QuerySet[Background]:
        qs = Background.objects.all().order_by('-start_year')
        if background_type:
            qs = qs.filter(background_type=background_type)
        offset = (page - 1) * page_size
        return qs[offset:offset + page_size]

    @staticmethod
    def create_background(**kwargs) -> Background:
        return Background.objects.create(**kwargs)

    @staticmethod
    def update_background(background: Background, **fields) -> Background:
        for attr, value in fields.items():
            setattr(background, attr, value)
        background.save()
        return background

    @staticmethod
    def soft_delete(background: Background) -> None:
        background.delete()

    @staticmethod
    def hard_delete(background: Background) -> None:
        background.hard_delete()

    @staticmethod
    def restore(background: Background) -> None:
        background.restore()


class CommunityLeaderDAO:
    """
    DAO for CommunityLeader role operations.
    """
    GROUP_NAME = 'CommunityLeader'

    @staticmethod
    def _get_group() -> Optional[Group]:
        try:
            return Group.objects.get(name=CommunityLeaderDAO.GROUP_NAME)
        except Group.DoesNotExist:
            return None

    @staticmethod
    def list_all() -> QuerySet[Member]:
        group = CommunityLeaderDAO._get_group()
        if not group:
            return Member.objects.none()
        return Member.objects.filter(groups=group)

    @staticmethod
    def get_by_id(member_id: str) -> Optional[Member]:
        return CommunityLeaderDAO.list_all().filter(id=member_id).first()

    @staticmethod
    def add_leader(member: Member) -> bool:
        group = CommunityLeaderDAO._get_group()
        if not group:
            return False
        member.groups.add(group)
        return True

    @staticmethod
    def remove_leader(member: Member) -> bool:
        group = CommunityLeaderDAO._get_group()
        if not group:
            return False
        member.groups.remove(group)
        return True


class AdministratorDAO:
    """
    DAO for Administrator role operations.
    """
    GROUP_NAME = 'Administrator'

    @staticmethod
    def _get_group() -> Optional[Group]:
        try:
            return Group.objects.get(name=AdministratorDAO.GROUP_NAME)
        except Group.DoesNotExist:
            return None

    @staticmethod
    def list_all() -> QuerySet[Member]:
        group = AdministratorDAO._get_group()
        if not group:
            return Member.objects.none()
        return Member.objects.filter(groups=group)

    @staticmethod
    def get_by_id(member_id: str) -> Optional[Member]:
        return AdministratorDAO.list_all().filter(id=member_id).first()

    @staticmethod
    def add_admin(member: Member) -> bool:
        group = AdministratorDAO._get_group()
        if not group:
            return False
        member.groups.add(group)
        return True

    @staticmethod
    def remove_admin(member: Member) -> bool:
        group = AdministratorDAO._get_group()
        if not group:
            return False
        member.groups.remove(group)
        return True
