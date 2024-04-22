package com.devsplan.ketchup.common;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDTO {
    private int status;
    private String message;
    private Object data;
}
