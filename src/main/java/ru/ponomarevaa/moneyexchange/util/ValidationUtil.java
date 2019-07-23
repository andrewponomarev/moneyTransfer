package ru.ponomarevaa.moneyexchange.util;

import ru.ponomarevaa.moneyexchange.exception.AlreadyExistsException;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.AbstractBaseEntity;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, long id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, long id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new AlreadyExistsException(entity + " must be new (id=null)");
        }
    }

}
