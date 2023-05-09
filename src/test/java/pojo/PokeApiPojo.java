package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

public class PokeApiPojo {

    private int count;
    private String next;
    private String previous;

    private List<PokeApiResultPojo> results;
}
