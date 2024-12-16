package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ComplaintBean;
import com.example.demo.repository.ComplaintRepository;


@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository repository;

    public ComplaintBean saveComplaint(ComplaintBean complaint) {

        return repository.save(complaint);
    }
}
