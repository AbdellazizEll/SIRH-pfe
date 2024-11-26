package com.example.anywrpfe.events;


import com.example.anywrpfe.entities.Collaborateur;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Collaborateur collaborateur;
    private String verificationUrl;

    public RegistrationCompleteEvent(Collaborateur collaborateur, String verificationUrl) {
        super(collaborateur);
        this.collaborateur = collaborateur;
        this.verificationUrl = verificationUrl;
    }
}
