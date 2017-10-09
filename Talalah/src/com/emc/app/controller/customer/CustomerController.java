package com.emc.app.controller.customer;

import java.io.IOException;

import org.apache.tika.mime.MimeTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.emc.app.entity.Entity;
import com.emc.app.entity.customer.Customer;

public interface CustomerController {
	public ResponseEntity<Entity> upload(String id, MultipartFile file) throws IOException, MimeTypeException;
	public ResponseEntity<Entity> updateProfile(Customer customer) throws IOException;
}
