package ru.aberezhnoy.homework4.commons.message;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        property = "type"
)

public abstract class Message implements Serializable {
}
