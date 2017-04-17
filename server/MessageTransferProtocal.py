from enum import Enum


class Type(Enum):
    Bind = 21
    Bind_Response = 22
    Sms = 31
    Sms_Response = 32

    @classmethod
    def get_type(cls, type_code):
        if type_code == 21:
            return Type.Bind
        elif type_code == 31:
            return Type.Sms
        else:
            return None


class Source(Enum):
    WeiXin = 1
    Phone = 2
    Server = 3


class WrapperMessage(object):
    def __init__(self):
        self.type = None
        self.message = None
        self.source = None


class BaseMessage(object):
    def __init__(self):
        self.phone = None
        self.model = None
        self.did = None


class BindMessage(BaseMessage):
    def __init__(self):
        BaseMessage.__init__(self)
        self.weiXinId = None
        self.code = None


class BindResponseMessage(object):
    def __init__(self, code, msg=""):
        self.code = code
        self.msg = msg


class SmsMessage(BaseMessage):
    def __init__(self):
        BaseMessage.__init__(self)
        self.sender = None
        self.body = None
        self.sign = None
        self.time = None


class SmsResponseMessage(object):
    def __init__(self, sign, code, msg=""):
        self.sign = sign
        self.code = code
        self.msg = msg
