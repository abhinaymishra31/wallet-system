package com.abhinay.walletsystem.repository;

import com.abhinay.walletsystem.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
}
