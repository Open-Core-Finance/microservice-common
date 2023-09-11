package tech.corefinance.common.test.support.aop;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AopTestController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/parserConfigurationException")
    public String parserConfigurationException() throws Exception {
        throw new ParserConfigurationException();
    }

    @GetMapping("/throwable")
    public String throwable() throws Throwable {
        throw new Throwable();
    }

    @GetMapping("/void")
    public void voidEndpoint(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
        response.getWriter().print(HttpStatus.MOVED_PERMANENTLY.name());
    }

    @GetMapping("/indexWithParam")
    public String indexWithParam(String param1, HttpServletRequest request, HttpServletResponse response,
                                 HttpSession httpSession) {
        return param1;
    }
}
