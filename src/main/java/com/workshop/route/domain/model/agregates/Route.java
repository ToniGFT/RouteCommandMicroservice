package com.workshop.route.domain.model.agregates;

import com.workshop.route.domain.model.entities.Schedule;
import com.workshop.route.domain.model.entities.Stop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "routes")
public class Route {

    @Id
    private String routeId;
    private String routeName;
    private List<Stop> stops;
    private Schedule schedule;

}

