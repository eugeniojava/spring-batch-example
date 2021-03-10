package com.eugenioamn.springbatchexample.batch;

import com.eugenioamn.springbatchexample.model.User;
import com.eugenioamn.springbatchexample.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) throws Exception {
        System.out.println("Data saved for users: " + users);
        userRepository.saveAll(users);
    }
}
