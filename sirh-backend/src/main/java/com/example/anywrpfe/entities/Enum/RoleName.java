//package com.example.Anywrpfe.Entities.Enum;
//
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//
//import java.io.Serializable;
//
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
//public enum RoleName implements Serializable {
//
//    ROLE_RH,
//    ROLE_MANAGER,
//    ROLE_COLLABORATOR;
//
//    @JsonSerialize(using = ToStringSerializer.class)
//    private String name;
//
//    RoleName(String name) {
//        this.name = name;
//    }
//
//    RoleName() {
//    }
//
//    public String getRoleNameName() {
//        return name;
//    }
//
//}
