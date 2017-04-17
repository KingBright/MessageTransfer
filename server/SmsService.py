import DbService


def add_new_sms(message):
    try:
        phone, result = DbService.get_phone(message)
        print(phone)
        try:
            sms, result = DbService.get_sms(phone.id, message)
            return result, "Successfully inserted the sms."
        except Exception as exc:
            print(exc)
            return False, "Failed to insert sms."
    except Exception as exc:
        print(exc)
        return False, "This phone can not be registered."
