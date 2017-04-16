import hashlib


def get_md5_value(src):
    md5 = hashlib.md5()
    md5.update(src)
    md5value = md5.hexdigest()
    return md5value
