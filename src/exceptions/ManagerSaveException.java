package exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {
        super();
    }

    public ManagerSaveException(String msg) {
        super(msg);
    }

    public ManagerSaveException(IOException e) {
        super(e);
    }
}
