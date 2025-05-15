from django.core.management.base import BaseCommand
from django.contrib.auth import get_user_model
from django.contrib.auth.models import Group, Permission
from django.contrib.contenttypes.models import ContentType

from communities.models import Community, Invite


class Command(BaseCommand):
    help = 'Initialize user roles and assign default permissions'

    def handle(self, *args, **options):
        User = get_user_model()

        # Content types for models
        member_ct = ContentType.objects.get_for_model(User)
        community_ct = ContentType.objects.get_for_model(Community)
        invite_ct = ContentType.objects.get_for_model(Invite)

        # Define roles and their permissions
        roles_permissions = {
            'CommunityLeader': [
                {'codename': 'add_community', 'content_type': community_ct, 'name': 'Can add community'},
                {'codename': 'change_community', 'content_type': community_ct, 'name': 'Can change community'},
                {'codename': 'delete_community', 'content_type': community_ct, 'name': 'Can delete community'},
                {'codename': 'approve_member', 'content_type': community_ct, 'name': 'Can approve member'},
                {'codename': 'unlock_member', 'content_type': community_ct, 'name': 'Can unlock member'},
                {'codename': 'restrict_member', 'content_type': community_ct, 'name': 'Can restrict member'},
                {'codename': 'add_invite', 'content_type': invite_ct, 'name': 'Can add invite'},
                {'codename': 'change_invite', 'content_type': invite_ct, 'name': 'Can change invite'},
                {'codename': 'delete_invite', 'content_type': invite_ct, 'name': 'Can delete invite'},
            ],
            'Administrator': [
                # All CommunityLeader perms
                *[
                    perm for perm in []  # placeholder, will be extended programmatically
                ],
                # Admin-specific perms
                {'codename': 'promote_to_leader', 'content_type': member_ct, 'name': 'Can promote to leader'},
                {'codename': 'demote_leader', 'content_type': member_ct, 'name': 'Can demote leader'},
                {'codename': 'reset_user_password', 'content_type': member_ct, 'name': 'Can reset user password'},
                {'codename': 'ban_member', 'content_type': member_ct, 'name': 'Can ban member'},
                {'codename': 'restrict_member_global', 'content_type': member_ct, 'name': 'Can globally restrict member'},
                # Also community CRUD
                {'codename': 'add_community', 'content_type': community_ct, 'name': 'Can add community'},
                {'codename': 'change_community', 'content_type': community_ct, 'name': 'Can change community'},
                {'codename': 'delete_community', 'content_type': community_ct, 'name': 'Can delete community'},
            ],
        }

        # Ensure groups and permissions exist
        for role_name, perms in roles_permissions.items():
            group, created = Group.objects.get_or_create(name=role_name)
            if created:
                self.stdout.write(f"Created group {role_name}")

            for perm_info in perms:
                perm, perm_created = Permission.objects.get_or_create(
                    codename=perm_info['codename'],
                    content_type=perm_info['content_type'],
                    defaults={'name': perm_info['name']}
                )
                if perm_created:
                    self.stdout.write(f"Created permission {perm_info['codename']}")
                group.permissions.add(perm)

            group.save()
            self.stdout.write(f"Assigned {len(perms)} permissions to group {role_name}")

        self.stdout.write(self.style.SUCCESS('Role initialization complete.'))
