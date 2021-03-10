package com.eugenioamn.springbatchexample.batch;

import com.eugenioamn.springbatchexample.model.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<User, User> {

    private static final Map<String, String> DEPARTMENT_NAMES = new HashMap<>();

    public Processor() {
        DEPARTMENT_NAMES.put("001", "Production");
        DEPARTMENT_NAMES.put("002", "Purchasing");
        DEPARTMENT_NAMES.put("003", "Marketing");
    }

    @Override
    public User process(User user) throws Exception {
        String departmentCode = user.getDepartment();
        String department = DEPARTMENT_NAMES.get(departmentCode);
        user.setDepartment(department);
        user.setTimestamp(new Date());
        System.out.printf(
                "Converted from [%s] to [%s]\n", departmentCode, department);

        return user;
    }
}
