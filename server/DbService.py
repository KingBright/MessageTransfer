from peewee import *
from playhouse.sqlite_ext import SqliteExtDatabase


class Phone(Model):
    id = PrimaryKeyField(unique=True)
    did = TextField(unique=True)
    phone = TextField(default="")
    model = TextField(default="")
    bind_code = TextField(default="")
    weixin = TextField(default="")
    bind_state = BooleanField(default=False)

    def set_bind_code(self, code):
        self.update(bind_code=code)

    def set_weixin(self, weixin):
        self.update(weixin=weixin)

    def set_bind_state(self, state):
        self.update(bind_state=state)

    @classmethod
    def get_phone(cls, did, phone="", model=""):
        return Phone.get_or_create(did=did, phone=phone, model=model)


class Sms(Model):
    id = PrimaryKeyField(unique=True)
    phone_id = ForeignKeyField(Phone)
    sender = TextField()
    body = TextField()
    time = TimeField(formats='%Y-%m-%d %H:%M:%S')
    transfer_state = BooleanField(default=False)

    def set_transfer_state(self, state):
        self.update(transfer_state=state)

    @classmethod
    def get_sms(cls, phone_id, sender, body, time):
        return Sms.get_or_create(phone_id=phone_id, sender=sender, body=body, time=time)


def get_phone(message):
    return Phone.get_phone(message.did, message.phone, message.model)


def get_sms(did, message):
    return Sms.get_sms(did, message.sender, message.body, message.time)


db = SqliteExtDatabase('message_transfer.db')
db.connect()
db.create_tables([Phone, Sms], safe=True)
