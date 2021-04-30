package client.argsWorker;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class DbIndexValidate implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        int index = Integer.parseInt(value);
        if (index < 0 || index > 1000) {
            throw new ParameterException("Index is invalid");
        }
    }
}
