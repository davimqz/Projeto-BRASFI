from django.shortcuts import redirect
from django.views.generic import TemplateView

def root_redirect(request):
    return redirect('http://localhost:3000')

class APIRootView(TemplateView):
    template_name = 'api_root.html' 