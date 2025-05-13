# BRASFI - Plataforma Social

## Requisitos
- Python 3.8+
- MySQL
- Node.js 14+
- npm ou yarn

## Configuração do Backend (Django)

1. Crie um ambiente virtual:
```bash
python -m venv venv
```

2. Ative o ambiente virtual:
- Windows:
```bash
venv\Scripts\activate
```
- Linux/Mac:
```bash
source venv/bin/activate
```

3. Instale as dependências:

```bash
pip install -r requirements.txt
```

4. Configure o banco de dados MySQL:
- Crie um banco de dados chamado 'brasfi_db'
- Configure as credenciais no arquivo .env

5. Execute as migrações:
```bash
python manage.py makemigrations
python manage.py migrate
```

6. Crie um superusuário:
```bash
python manage.py createsuperuser
```

7. Inicie o servidor:
```bash
python manage.py runserver
```

## Configuração do Frontend (React)

1. Entre na pasta do frontend:
```bash
cd frontend
```

2. Instale as dependências:
```bash
npm install
```

3. Inicie o servidor de desenvolvimento:
```bash
npm start
```

## Acessando a Aplicação
- Backend: http://localhost:8000
- Frontend: http://localhost:3000
- Admin: http://localhost:8000/admin 