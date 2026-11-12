package co.com.crediya.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRest.class);

    @GetMapping(path = "/usecase/path")
    public String commandName() {
        return "Value from use case v1";
    }

}
