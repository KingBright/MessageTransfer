import requests
import json

from WXBizDataCrypt import WXBizDataCrypt
import codecs

from poyo import parse_string, PoyoException

config = None


def load():
    with codecs.open('config.yaml', encoding='utf-8') as ymlfile:
        ymlstring = ymlfile.read()

    try:
        cfg = parse_string(ymlstring)
    except PoyoException as exc:
        print(exc.__cause__)
        return None
    else:
        print(cfg)
        return cfg


def get_url(app_id, secret, code):
    url = 'https://api.weixin.qq.com/sns/jscode2session'
    url = url + '?appid=' + app_id
    url = url + '&secret=' + secret
    url = url + '&js_code=' + code
    url += '&grant_type=authorization_code'
    return url


def decrypt(code, encrypted_data, iv):
    global config
    if config is None:
        config = load()

    app_id = config.get('appId')
    app_sec = config.get("appSec")

    result = requests.get(get_url(app_id, app_sec, code), verify=False)

    print(str(result.content))
    obj = json.loads(result.content)

    session_key = obj['session_key']

    pc = WXBizDataCrypt(app_id, session_key)
    print("start to decrypt")
    print(pc.decrypt(encrypted_data, iv))


def test():
    decrypt("aaa", "bbb", "ccc")


if __name__ == '__main__':
    test()
