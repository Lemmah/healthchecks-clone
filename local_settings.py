DATABASES = {
    'default': {
        'ENGINE':   'django.db.backends.postgresql',
        'HOST': 'healthchecksdbinstance.ca6ibazvkqva.us-east-1.rds.amazonaws.com',
        'NAME':     'hc',
        'USER':     'lemmah',
        'PASSWORD': 'Aminsecure2',
        'TEST': {'CHARSET': 'UTF8'}
    }
}
