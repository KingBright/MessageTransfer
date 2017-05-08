from enum import Enum


class Type(Enum):
    Login = 11
    Login_Response = 12
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
        elif type_code == 11:
            return Type.Login
        else:
            return None


class Source(Enum):
    WeiXin = 1
    Phone = 2
    Server = 3

    @classmethod
    def get_source(cls, source_code):
        if source_code == 1:
            return Source.WeiXin
        elif source_code == 2:
            return Source.Phone
        else:
            return None


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
        self.phone = None


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


class LoginMessage(object):
    def __init__(self):
        self.code = None
        self.rawData = None
        # 使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息，参考文档 signature。
        self.signature = None
        # 包括敏感数据在内的完整用户信息的加密数据，详细见加密数据解密算法
        self.encryptedData = None
        # 加密算法的初始向量
        self.iv = None


class LoginResponseMessage(object):
    def __init__(self, session):
        self.session = session
