package clearsolutions.com.javatestassignment.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertySourceResolver {
    @Value("${minAge}") private int minimalAge;

    public int getMinAge() {
        return minimalAge;
    }
}