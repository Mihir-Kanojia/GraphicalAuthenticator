

from cryptography.fernet import Fernet

def encryptUri(uri):
    key = Fernet.generate_key()
    f = Fernet(key)
    token = f.encrypt(uri)
    return f,token

def decryptUri(f,token):
    d = f.decrypt(token)
    return d

