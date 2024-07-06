package com.dal.asdc.reconnect.DTO.Chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageRequest
{
    String to;
    String context;
}
