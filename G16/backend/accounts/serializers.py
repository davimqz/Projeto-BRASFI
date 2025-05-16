from rest_framework import serializers
from accounts.models.member_model import Member, Background
from accounts.services.user_service import UserService


class BackgroundSerializer(serializers.ModelSerializer):
    """
    Serializer for Background entries.
    """
    class Meta:
        model = Background
        fields = ['id', 'title', 'institution', 'background_type', 'start_year', 'end_year']
        read_only_fields = ['id']


class MemberSerializer(serializers.ModelSerializer):
    """
    Serializer for Member entities.

    - Nested backgrounds read-only.
    - Validates CPF format and normalizes username/email.
    - Uses UserService for create/update operations.
    """
    backgrounds = BackgroundSerializer(many=True, read_only=True)
    username = serializers.CharField(required=True)
    email = serializers.EmailField(required=True)
    cpf = serializers.RegexField(
        regex=r'^\d{3}\.\d{3}\.\d{3}-\d{2}$',
        error_messages={'invalid': 'CPF must be in XXX.XXX.XXX-XX format'},
        required=True
    )
    password = serializers.CharField(write_only=True, required=True, min_length=8)

    class Meta:
        model = Member
        fields = [
            'id', 'username', 'email', 'cpf', 'first_name', 'last_name',
            'phone', 'description', 'date_joined', 'is_active', 'is_staff',
            'backgrounds', 'password'
        ]
        read_only_fields = ['id', 'date_joined', 'is_active', 'is_staff']

    def validate_username(self, value):
        """Normalize username to lowercase and trim whitespace."""
        return value.lower().strip()

    def validate_email(self, value):
        """Normalize email to lowercase and trim whitespace."""
        return value.lower().strip()

    def create(self, validated_data):
        # Delegate creation to the service layer
        return UserService.register_user(validated_data)

    def update(self, instance, validated_data):
        # Delegate update to the service layer
        return UserService.update_user(instance.id, validated_data)


class CommunityLeaderSerializer(MemberSerializer):
    """
    Serializer for Community Leader entities.

    Inherits from MemberSerializer; used when listing or retrieving community leaders.
    """
    class Meta(MemberSerializer.Meta):
        pass


class AdministratorSerializer(MemberSerializer):
    """
    Serializer for Administrator entities.

    Inherits from MemberSerializer; used when listing or retrieving administrators.
    """
    class Meta(MemberSerializer.Meta):
        pass
