package org.fdu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to map static web pages such as Jira requests
 *    this allows users to avoid typing index.html, etc.
 */

@Controller
public class SpringBootStaticController {
    @GetMapping("/request/")
    public String requestIndex() {
        return "forward:/request/index.html";
    }

    @GetMapping("/request")
    public String requestNoSlash() {
        return "redirect:/request/";
    }
}
