package edu.ucsb.cs156.happiercows.entities;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
// import java.time.LocalDateTime;
// import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String name;
    private String term;

    // @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    // @JsonIgnore
    // private List<Student> students;
}
