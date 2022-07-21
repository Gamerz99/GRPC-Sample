package com.demo.lk.userAddressService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDto {
	private String userId;
	private String addressLine;
	private String city;
	private String country;
}
