package jsug.app.account;

import jsug.domain.model.Account;
import jsug.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("account")
public class AccountController {
    @Autowired // (1)
            AccountService accountService;

    @ModelAttribute // (2)
    AccountForm setupForm() {
        return new AccountForm();
    }

    @RequestMapping(value = "create", params = "form", method = RequestMethod.GET) // (3)
    String createForm() {
        return "account/createForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST) // (4)
    String create(@Validated AccountForm form /* (5) */, BindingResult bindingResult,
                  RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) { // (6)
            return "account/createForm";
        }
        Account account = Account.builder() // (7)
                .name(form.getName())
                .email(form.getEmail())
                .birthDay(form.getBirthDay())
                .zip(form.getZip())
                .address(form.getAddress())
                .build();
        accountService.register(account, form.getPassword()); // (8)
        attributes.addFlashAttribute(account); // (9)
        return "redirect:/account/create?finish"; // (10)
    }

    @RequestMapping(value = "create", params = "finish", method = RequestMethod.GET) // (11)
    String createFinish() {
        return "account/createFinish";
    }
}