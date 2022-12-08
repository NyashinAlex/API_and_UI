package models.scenario;

import lombok.Data;

@Data
public class WorkPath {

    private int workPath;

    public WorkPath(int workPath) {
        this.workPath = workPath;
    }
}
