from typing import Dict, Optional
from django.core.exceptions import PermissionDenied, ObjectDoesNotExist
from accounts.dao.member_dao import (
    MemberDAO,
    CommunityLeaderDAO,
    AdministratorDAO,
    BackgroundDAO
)
from accounts.models.member_model import Member, Background


class UserService:
    """
    Business logic for user operations, coordinating between DAOs and enforcing rules.
    """

    @staticmethod
    def register_user(data: Dict) -> Member:
        # Unpack required fields
        username = data.get('username')
        email = data.get('email')
        cpf = data.get('cpf')
        first_name = data.get('first_name')
        last_name = data.get('last_name')
        password = data.get('password')

        # Validate uniqueness
        if MemberDAO.exists_by_username(username):
            raise ValueError('Username already exists')
        if MemberDAO.exists_by_email(email):
            raise ValueError('Email already exists')
        if MemberDAO.exists_by_cpf(cpf):
            raise ValueError('CPF already exists')

        # Create member and return
        return MemberDAO.create_member(
            username=username,
            email=email,
            cpf=cpf,
            first_name=first_name,
            last_name=last_name,
            password=password
        )

    @staticmethod
    def get_user(member_id: str) -> Optional[Member]:
        return MemberDAO.get_by_id(member_id)

    @staticmethod
    def update_user(member_id: str, fields: Dict) -> Member:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        return MemberDAO.update_member(member, **fields)

    @staticmethod
    def delete_user(member_id: str) -> None:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        MemberDAO.soft_delete(member)

    @staticmethod
    def restore_user(member_id: str) -> None:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        MemberDAO.restore(member)

    @staticmethod
    def add_background_to_user(member_id: str, background_data: Dict) -> Background:
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        background = BackgroundDAO.create_background(**background_data)
        MemberDAO.add_background(member, background)
        return background

    @staticmethod
    def remove_background_from_user(member_id: str, background_id: str) -> None:
        member = MemberDAO.get_by_id(member_id)
        background = BackgroundDAO.get_by_id(background_id)
        if not member or not background:
            raise ObjectDoesNotExist('Member or Background not found')
        MemberDAO.remove_background(member, background)

    @staticmethod
    def list_users(**filters) -> Optional[list]:
        # Delegates to DAO's list_all
        return MemberDAO.list_all(
            is_active=filters.get('is_active'),
            background_id=filters.get('background_id'),
            page=filters.get('page', 1),
            page_size=filters.get('page_size', 20),
            order_by=filters.get('order_by', '-created_at')
        )

    @staticmethod
    def promote_to_leader(admin_id: str, member_id: str) -> bool:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can promote leaders')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        return CommunityLeaderDAO.add_leader(member)

    @staticmethod
    def demote_leader(admin_id: str, leader_id: str) -> bool:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can demote leaders')
        leader = CommunityLeaderDAO.get_by_id(leader_id)
        if not leader:
            raise ObjectDoesNotExist('CommunityLeader not found')
        return CommunityLeaderDAO.remove_leader(leader)

    @staticmethod
    def ban_member(admin_id: str, member_id: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can ban members')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        MemberDAO.hard_delete(member)

    @staticmethod
    def restrict_member(admin_id: str, member_id: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can restrict members')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        MemberDAO.soft_delete(member)

    @staticmethod
    def reset_password(admin_id: str, member_id: str, new_password: str) -> None:
        admin = AdministratorDAO.get_by_id(admin_id)
        if not admin:
            raise PermissionDenied('Only administrators can reset passwords')
        member = MemberDAO.get_by_id(member_id)
        if not member:
            raise ObjectDoesNotExist('Member not found')
        MemberDAO.update_member(member, password=new_password)
