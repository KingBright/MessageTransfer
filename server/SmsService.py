import DbService


def add_new_sms(message):
    try:
        phone, result = DbService.get_phone(message)
        try:
            sms, result = DbService.get_sms(phone.id, message)
            if result is True:
                print("sms inserted")
                return result, "Successfully inserted the sms."
            else:
                print("Already inserted")
                return result, "Sms already inserted."
        except Exception as exc:
            print(exc)
            return False, "Failed to insert sms."
    except Exception as exc:
        print(exc)
        return False, "This phone can not be registered."
