from typing import Dict, Optional
from django.core.exceptions import ValidationError, PermissionDenied
from django.db.models import QuerySet
from accounts.dao.member_dao import (
    MemberDAO,
    BackgroundDAO,
    CommunityLeaderDAO,
    AdministratorDAO
)
from accounts.models.member_model import Member, Background


class UserNotFoundException(Exception):
    """
    Exception raised when a Member is not found.
    """
    pass


class UserService:
    """
    Service for Member operations.

    Responsibilities:
      - Register, retrieve, update, delete, and restore members.
      - Manage user listings with filtering and pagination.

    Exceptions:
      - ValidationError for invalid input or duplicate data.
      - UserNotFoundException when a member is not found.
    """

    @staticmethod
    def register_user(data: Dict) -> Member:
        # Validate required fields
        required = ['username', 'email', 'cpf', 'first_name', 'last_name', 'password']
        for field in required:
            value = data.get(field)
            if not value or not isinstance(value, str) or not value.strip():
                raise ValidationError(f"Field '{field}' is required and must be non-empty.")

        username = data['username'].strip()
        email = data['email'].strip()
        cpf = data['cpf'].strip()
        first_name = data['first_name'].strip()
        last_name = data['last_name'].strip()
        password = data['password']

        # Validate uniqueness
        if MemberDAO.exists_by_username(username):
            raise ValidationError('Username already exists')
        if MemberDAO.exists_by_email(email):
            raise ValidationError('Email already exists')
        if MemberDAO.exists_by_cpf(cpf):
            raise ValidationError('CPF already exists')

        # Create member
        return MemberDAO.create_member(
            username=username,
            email=email,
            cpf=cpf,
            first_name=first_name,
            last_name=last_name,
            password=password
        )

    @staticmethod
    def get_user(member_id: str) -> Member:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        return member

    @staticmethod
    def update_user(member_id: str, fields: Dict) -> Member:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        return MemberDAO.update_member(member, **fields)

    @staticmethod
    def delete_user(member_id: str) -> None:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        MemberDAO.soft_delete(member)

    @staticmethod
    def restore_user(member_id: str) -> None:
        member = MemberDAO.get_all_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        MemberDAO.restore(member)

    @staticmethod
    def list_users(
        is_active: Optional[bool] = None,
        background_id: Optional[str] = None,
        page: int = 1,
        page_size: int = 20,
        order_by: str = '-created_at'
    ) -> QuerySet:
        return MemberDAO.list_all(
            is_active=is_active,
            background_id=background_id,
            page=page,
            page_size=page_size,
            order_by=order_by
        )


class BackgroundService:
    """
    Service for Background operations.

    Responsibilities:
      - List, retrieve, create, update, delete, and restore backgrounds.
    """

    @staticmethod
    def list_backgrounds(
        background_type: Optional[str] = None,
        page: int = 1,
        page_size: int = 20
    ) -> QuerySet:
        return BackgroundDAO.list_all(
            background_type=background_type,
            page=page,
            page_size=page_size
        )

    @staticmethod
    def get_background(background_id: str) -> Background:
        background = BackgroundDAO.get_by_id(background_id)
        if not background:
            raise ObjectDoesNotExist(f"Background with id '{background_id}' not found.")
        return background

    @staticmethod
    def create_background(data: Dict) -> Background:
        # data validation can be added here
        return BackgroundDAO.create_background(**data)

    @staticmethod
    def update_background(background_id: str, data: Dict) -> Background:
        background = BackgroundDAO.get_by_id(background_id)
        if not background:
            raise ObjectDoesNotExist(f"Background with id '{background_id}' not found.")
        return BackgroundDAO.update_background(background, **data)

    @staticmethod
    def delete_background(background_id: str) -> None:
        background = BackgroundDAO.get_by_id(background_id)
        if not background:
            raise ObjectDoesNotExist(f"Background with id '{background_id}' not found.")
        BackgroundDAO.soft_delete(background)

    @staticmethod
    def restore_background(background_id: str) -> None:
        background = BackgroundDAO.get_by_id(background_id)
        if not background:
            raise ObjectDoesNotExist(f"Background with id '{background_id}' not found.")
        BackgroundDAO.restore(background)


class CommunityLeaderService:
    """
    Service for CommunityLeader role operations.

    Responsibilities:
      - List, retrieve, assign, and revoke community leaders.
    Authorization:
      - Only administrators may assign or revoke leaders.
    """

    @staticmethod
    def list_leaders() -> QuerySet:
        return CommunityLeaderDAO.list_all()

    @staticmethod
    def get_leader(leader_id: str) -> Member:
        leader = CommunityLeaderDAO.get_by_id(leader_id)
        if not leader:
            raise ObjectDoesNotExist(f"CommunityLeader with id '{leader_id}' not found.")
        return leader

    @staticmethod
    def assign_leader(admin_id: str, member_id: str) -> bool:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can assign community leaders')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        success = CommunityLeaderDAO.add_leader(member)
        if not success:
            raise ValueError('CommunityLeader group not initialized')
        return success

    @staticmethod
    def revoke_leader(admin_id: str, leader_id: str) -> bool:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can revoke community leaders')
        leader = CommunityLeaderDAO.get_by_id(leader_id)
        if not leader:
            raise ObjectDoesNotExist(f"CommunityLeader with id '{leader_id}' not found.")
        success = CommunityLeaderDAO.remove_leader(leader)
        if not success:
            raise ValueError('CommunityLeader group not initialized')
        return success


class AdministratorService:
    """
    Service for Administrator role operations.

    Responsibilities:
      - List, retrieve, add, and remove administrators.
      - Ban or restrict members.
      - Reset member passwords.
    Authorization:
      - Only administrators may perform these actions.
    """

    @staticmethod
    def list_admins() -> QuerySet:
        return AdministratorDAO.list_all()

    @staticmethod
    def get_admin(admin_id: str) -> Member:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise ObjectDoesNotExist(f"Administrator with id '{admin_id}' not found.")
        return admin

    @staticmethod
    def add_admin(member_id: str) -> bool:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        success = AdministratorDAO.add_admin(member)
        if not success:
            raise ValueError('Administrator group not initialized')
        return success

    @staticmethod
    def remove_admin(admin_id: str) -> bool:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise ObjectDoesNotExist(f"Administrator with id '{admin_id}' not found.")
        success = AdministratorDAO.remove_admin(admin)
        if not success:
            raise ValueError('Administrator group not initialized')
        return success

    @staticmethod
    def ban_member(admin_id: str, member_id: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can ban members')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        MemberDAO.hard_delete(member)

    @staticmethod
    def restrict_member(admin_id: str, member_id: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can restrict members')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        MemberDAO.soft_delete(member)

    @staticmethod
    def reset_password(admin_id: str, member_id: str, new_password: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can reset passwords')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise UserNotFoundException(f"Member with id '{member_id}' not found.")
        MemberDAO.update_member(member, password=new_password)
