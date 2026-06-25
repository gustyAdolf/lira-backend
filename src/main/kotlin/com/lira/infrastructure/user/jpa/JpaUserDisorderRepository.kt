package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.UserDisorderEntity
import com.lira.infrastructure.user.UserDisorderId
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserDisorderRepository : JpaRepository<UserDisorderEntity, UserDisorderId>
