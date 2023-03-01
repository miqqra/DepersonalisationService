package ru.nsu.backend.person;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "people")
public class People {
    private List<? extends Person> people;

    public List<? extends Person> getPeople(){
        return people;
    }
}
