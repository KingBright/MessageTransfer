import DbService


def add_new_sms(message):
    try:
        phone = DbService.get_phone(message)
        try:
            sms, result = DbService.get_sms(message, phone.did)
            return result, "Successfully inserted the sms."
        except:
            return False, "Failed to insert sms."
    except:
        return False, "This phone can not be registered."
