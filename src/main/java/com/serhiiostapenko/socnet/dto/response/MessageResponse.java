package com.serhiiostapenko.socnet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;

    public MessageResponse(Collection<String> values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : values) {
            stringBuilder.append(str).append('\n');
        }
        this.message = stringBuilder.toString();
    }
}
