package models.scenario;

import lombok.Data;

@Data
public class Steps {

    private String name;
    private String spacing;

    public Steps(String name, String spacing) {
        this.name = name;
        this.spacing = spacing;
    }
}
