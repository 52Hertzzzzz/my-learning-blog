package com.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    String userName;

    String message;

    String stuffId;

    Integer stuffAmount;

    Integer moneyAmount;

}
