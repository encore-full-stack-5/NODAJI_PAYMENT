package com.nodaji.payment.controller;

import com.nodaji.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public String paymentResult(
            Model model,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey) throws Exception {

        Map<String, Object> result = paymentService.processPayment(orderId, amount, paymentKey);
        boolean isSuccess = (boolean) result.get("isSuccess");
        JSONObject jsonObject = (JSONObject) result.get("response");

        model.addAttribute("isSuccess", isSuccess);
        model.addAttribute("responseStr", jsonObject.toJSONString());

        if (isSuccess) {
            model.addAttribute("method", (String) jsonObject.get("method"));
            model.addAttribute("orderName", (String) jsonObject.get("orderName"));

            String method = (String) jsonObject.get("method");
            if (method != null) {
                switch (method) {
                    case "카드":
                        model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
                        break;
                    case "가상계좌":
                        model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
                        break;
                    case "계좌이체":
                        model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
                        break;
                    case "휴대폰":
                        model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
                        break;
                }
            } else {
                model.addAttribute("code", (String) jsonObject.get("code"));
                model.addAttribute("message", (String) jsonObject.get("message"));
            }
        }

        return "success";
    }

    @GetMapping("/fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code) {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "fail";
    }

}