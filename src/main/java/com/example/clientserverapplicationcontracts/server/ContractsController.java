package com.example.clientserverapplicationcontracts.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

@Controller
@ResponseBody
public class ContractsController {
    @PostMapping("/contracts")
    public List<Contract> showContracts(@RequestBody String request){
        if(Objects.equals(request, "SELECT CONTRACTS")){
            ContractManager cm = new ContractManager();
            cm.init();
            return cm.getAllContracts();
        } else {
            return null;
        }
    }
}
