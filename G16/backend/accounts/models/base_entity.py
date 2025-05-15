import uuid
from django.db import models
from django.forms.models import model_to_dict
from django.utils import timezone


class BaseEntityQuerySet(models.QuerySet):
    """
    Custom QuerySet to handle soft-deletion and restoration in bulk.
    """
    def delete(self):
        # Soft-delete all objects in the queryset
        return super().update(is_deleted=True, deleted_at=timezone.now())

    def hard_delete(self):
        # Permanently remove all objects
        return super().delete()

    def restore(self):
        # Restore soft-deleted objects
        return super().update(is_deleted=False, deleted_at=None)


class BaseEntityManager(models.Manager):
    """
    Manager that filters out soft-deleted objects by default.
    """
    def get_queryset(self):
        return BaseEntityQuerySet(self.model, using=self._db).filter(is_deleted=False)

    def all_objects(self):
        # Return queryset including soft-deleted objects
        return BaseEntityQuerySet(self.model, using=self._db)


class BaseEntity(models.Model):
    """
    Abstract base class providing:
      - UUID primary key (id)
      - created_at / updated_at timestamps
      - soft-delete functionality
      - helper methods
    """
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    deleted_at = models.DateTimeField(null=True, blank=True)

    # Managers
    objects = BaseEntityManager()
    all_objects = models.Manager()

    class Meta:
        abstract = True
        ordering = ['-created_at']

    def delete(self, using=None, keep_parents=False):
        """
        Soft delete this instance.
        """
        self.is_deleted = True
        self.deleted_at = timezone.now()
        self.save(update_fields=['is_deleted', 'deleted_at'])

    def hard_delete(self, using=None, keep_parents=False):
        """
        Permanently delete this instance from the database.
        """
        super(BaseEntity, self).delete(using=using, keep_parents=keep_parents)

    def restore(self):
        """
        Restore a soft-deleted instance.
        """
        self.is_deleted = False
        self.deleted_at = None
        self.save(update_fields=['is_deleted', 'deleted_at'])

    def to_dict(self, include_deleted=False):
        """
        Returns a dict representation of the model.
        By default excludes soft-delete flags; set include_deleted=True to include them.
        """
        data = model_to_dict(self)
        if not include_deleted:
            data.pop('is_deleted', None)
            data.pop('deleted_at', None)
        return data
