package models.scenario;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CreateTestScenarioBody {

    public ArrayList<Steps> steps;
    public ArrayList<Integer> workPath;

    public CreateTestScenarioBody(ArrayList<Steps> steps, ArrayList<Integer> workPath) {
        this.steps = steps;
        this.workPath = workPath;
    }
}