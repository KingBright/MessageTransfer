import json


def to_json(obj):
    return json.dumps(obj.__dict__)


def to_obj(json_str, cls):
    instance = cls()
    if type(json_str) is not dict:
        _dict = json.loads(json_str)
    else:
        _dict = json_str
    instance.__dict__.update(_dict)
    return instance
