import uuid
from django.db import models
from django.forms.models import model_to_dict
from django.utils import timezone


class BaseEntity(models.Model):
    """
    Abstract base class providing:
      - UUID PK
      - created/updated timestamps
      - soft-delete functionality
      - helper methods
    """
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    deleted_at = models.DateTimeField(null=True, blank=True)

    class Meta:
        abstract = True
        ordering = ['-created_at']

    def delete(self, using=None, keep_parents=False, soft=True):
        """
        Soft delete: mark as deleted without removing from DB.
        If soft=False, performs hard delete.
        """
        if soft:
            self.is_deleted = True
            self.deleted_at = timezone.now()
            self.save(update_fields=['is_deleted', 'deleted_at'])
        else:
            super(BaseEntity, self).delete(using=using, keep_parents=keep_parents)

    def hard_delete(self, using=None, keep_parents=False):
        """
        Permanently delete the object from DB.
        """
        super(BaseEntity, self).delete(using=using, keep_parents=keep_parents)

    def restore(self):
        """
        Restore a soft-deleted object.
        """
        self.is_deleted = False
        self.deleted_at = None
        self.save(update_fields=['is_deleted', 'deleted_at'])

    def to_dict(self):
        """
        Returns a dict representation of the model, excluding soft-delete flags.
        """
        data = model_to_dict(self)
        data.pop('is_deleted', None)
        data.pop('deleted_at', None)
        return data
